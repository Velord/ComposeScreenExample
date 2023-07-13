package com.example.windyappflowsummator

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

private const val SPLIT_FLOW_CREATION_BY_CHUNK = 10000
private const val WAIT_FOR_BEFORE_ADD_NEW_CHUNK_OF_FLOWS = 10L
private const val DELAY_BEFORE_EMIT = 100L

class FlowCreator(
    private val countOfFlowToCreate: Int,
    private val splitCreatingBy: Int = SPLIT_FLOW_CREATION_BY_CHUNK,
    private val paralellism: Boolean = true,
    private val onEmit: suspend (Int) -> Unit,
) {
    // It is necessary to create an array of Flow<Int> of N.
    private val flows = mutableListOf<Flow<Int>>()

    fun start(scope: CoroutineScope): Job = scope.launch {
        val ranges = if (paralellism) {
            createRanges()
        } else {
            listOf(IntRange(0, countOfFlowToCreate))
        }
        ranges.map {
            // Waiting gives more precise putting flows to the list
            delay(WAIT_FOR_BEFORE_ADD_NEW_CHUNK_OF_FLOWS)
            scope.async {
                flows.addAll(createFlowsByRange(it))
            }
        }.awaitAll()
        launchAllFlow(flows.toTypedArray())
    }

    private fun createRanges(): List<IntRange> {
        val ranges = mutableListOf<IntRange>()
        if (countOfFlowToCreate > splitCreatingBy) {
            val countOfRanges = countOfFlowToCreate / splitCreatingBy
            repeat(countOfRanges) { index ->
                val start = index * splitCreatingBy
                val end = start + splitCreatingBy
                ranges += IntRange(start, end)
            }
            if (countOfFlowToCreate % splitCreatingBy != 0) {
                ranges += IntRange(countOfRanges * splitCreatingBy, countOfFlowToCreate)
            }
        } else {
            ranges += IntRange(0, countOfFlowToCreate)
        }
        return ranges
    }

    private suspend fun createFlowsByRange(indexRange: IntRange): MutableList<Flow<Int>> {
        val flows = mutableListOf<Flow<Int>>()
        val countOfFlowToCreate = indexRange.last - indexRange.first
        repeat(countOfFlowToCreate) { index ->
            yield()
            val shift = indexRange.first + index
            flows += flow {
                // After a delay of (index + 1) * 100
                val waitFor = (shift + 1) * DELAY_BEFORE_EMIT
                delay(waitFor)
                // Emits the value index + 1
                emit(shift + 1)
            }
        }
        return flows
    }

    private fun CoroutineScope.launchAllFlow(flows: Array<Flow<Int>>) {
        flows.forEach { flow ->
            launch {
                flow.collect { newNumber ->
                    ensureActive()
                    onEmit(newNumber)
                }
            }
        }
    }
}