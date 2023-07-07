package com.example.windyappflowsummator

import com.velord.util.viewModel.BaseViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private data class EmitNumber(
    val newValue: Int,
    val previousValue: Int
) {
    val sum: Int = newValue + previousValue
}

class FlowSummatorViewModel : BaseViewModel() {

    val currentTextFlow: MutableStateFlow<String> = MutableStateFlow("")
    val currentEnteredNumberFlow: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val sumFlow: MutableSharedFlow<EmitNumber> = MutableSharedFlow(
        replay = 1,
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    private val launchSumFlow: MutableSharedFlow<Int> = MutableSharedFlow()

    init {
        observeSumFlow()
        observeSumLaunchFlow()
    }

    fun onStartClick() = launch {
        // Case when user didn't enter anything
        if (currentEnteredNumberFlow.value == null) return@launch

        currentEnteredNumberFlow.value?.let {
            launchSumFlow.emit(it)
        }
    }

    fun onNewEnteredValue(newValue: String) {
        val calculatedValue = if (newValue.toIntOrNull() == null) {
            // Case when user entered not a number(from clipboard framework)
            // or empty string
            // or that is not a number(Int in that case)
            if (newValue.isEmpty()) null // Case when user cleared the field
            else currentEnteredNumberFlow.value
        } else {
            newValue.toIntOrNull()
        }

        currentEnteredNumberFlow.value = calculatedValue
    }

    private fun getPrevEmittedValue(): Int =
        sumFlow.replayCache.firstOrNull()?.sum ?: 0

    private fun createFlows(countOfFlowToCreate: Int): Array<Flow<Int>> {
        val flows = mutableListOf<Flow<Int>>()
        // Необходимо создать массив Flow<Int>, количества N
        repeat(countOfFlowToCreate) { index ->
            flows += flow {
                // после задержки в (index + 1) * 100
                val waitFor = (index + 1) * 100L
                delay(waitFor)
                // эмитит значение index + 1
                emit(index + 1)
            }
        }
        return flows.toTypedArray()
    }

    private fun observeSumFlow() = launch {
        sumFlow.collect { newNumber ->
            // Суммирующий Flow должен возвращать значение после обновления каждого из N Flow.
            currentTextFlow.update {
                it + "\n" + newNumber.sum
            }
        }
    }

    private fun observeSumLaunchFlow() = launch {
        launchSumFlow.collectLatest { flowCount ->
            // Reset state
            currentTextFlow.value = ""
            sumFlow.resetReplayCache()

            val flows = createFlows(flowCount)
            // Результирующий Flow должен суммировать значения всех N Flow.
            coroutineScope {
                flows.forEach { flow ->
                    launch {
                        flow.collect { newNumber ->
                            val number = EmitNumber(
                                previousValue = getPrevEmittedValue(),
                                newValue = newNumber
                            )
                            sumFlow.emit(number)
                        }
                    }
                }
            }
        }
    }
}