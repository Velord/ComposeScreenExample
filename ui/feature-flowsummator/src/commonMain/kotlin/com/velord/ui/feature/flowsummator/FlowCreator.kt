package com.velord.ui.feature.flowsummator

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
private const val WAIT_BEFORE_ADDING_NEW_FLOW_CHUNK = 10L
private const val DELAY_BEFORE_EMIT = 100L

class FlowCreator(
    private val flowCountToCreate: Int,
    private val splitCreatingBy: Int = SPLIT_FLOW_CREATION_BY_CHUNK,
    private val parallelism: Boolean = true,
    private val onEmit: suspend (Int) -> Unit,
) {
    // It is necessary to create an array of Flow<Int> of N.
    private val flowRoster = mutableListOf<Flow<Int>>()

    fun start(scope: CoroutineScope): Job = scope.launch {
        val rangeRoster = if (parallelism) {
            createRangeRoster()
        } else {
            listOf(IntRange(0, flowCountToCreate))
        }
        rangeRoster.map {
            // Waiting gives more precise putting flows to the list
            delay(WAIT_BEFORE_ADDING_NEW_FLOW_CHUNK)
            scope.async {
                flowRoster.addAll(createFlowRosterByRange(it))
            }
        }.awaitAll()
        launchAllFlow(flowRoster.toTypedArray())
    }

    private fun createRangeRoster(): List<IntRange> {
        val rangeRoster = mutableListOf<IntRange>()
        if (flowCountToCreate > splitCreatingBy) {
            val rangeCount = flowCountToCreate / splitCreatingBy
            repeat(rangeCount) { index ->
                val start = index * splitCreatingBy
                val end = start + splitCreatingBy
                rangeRoster += IntRange(start, end)
            }
            if (flowCountToCreate % splitCreatingBy != 0) {
                rangeRoster += IntRange(rangeCount * splitCreatingBy, flowCountToCreate)
            }
        } else {
            rangeRoster += IntRange(0, flowCountToCreate)
        }

        return rangeRoster
    }

    private suspend fun createFlowRosterByRange(indexRange: IntRange): MutableList<Flow<Int>> {
        val flowRoster = mutableListOf<Flow<Int>>()
        val flowCountToCreate = indexRange.last - indexRange.first
        repeat(flowCountToCreate) { index ->
            yield()
            val shift = indexRange.first + index
            flowRoster += flow {
                // After a delay of (index + 1) * 100
                val waitFor = (shift + 1) * DELAY_BEFORE_EMIT
                delay(waitFor)
                // Emits the value index + 1
                emit(shift + 1)
            }
        }

        return flowRoster
    }

    private fun CoroutineScope.launchAllFlow(flowRoster: Array<Flow<Int>>) {
        flowRoster.forEach { flow ->
            launch {
                flow.collect { newNumber ->
                    ensureActive()
                    onEmit(newNumber)
                }
            }
        }
    }
}
