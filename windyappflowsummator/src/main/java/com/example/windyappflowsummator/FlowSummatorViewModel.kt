package com.example.windyappflowsummator

import android.util.Log
import com.velord.util.viewModel.BaseViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
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
    private val emitNumberFlow: MutableSharedFlow<EmitNumber> = MutableSharedFlow(
        replay = 1,
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    init {
        observeEmitNumberFlow()
    }

    fun onStartClick() {
        if (currentEnteredNumberFlow.value == null) return // Case when user didn't enter anything
        if (currentEnteredNumberFlow.value == 0) return // Case when user entered 0

        currentEnteredNumberFlow.value?.let {
            launchSummator(it)
        }
    }

    fun onNewEnteredValue(newValue: String) {
        val calculatedValue = if (newValue.toIntOrNull() == null) {
            // Case when user entered not a number(from clipboard framework)
            // or empty string
            // or that is not a number(BigInteger in that case)
            if (newValue.isEmpty()) null // Case when user cleared the field
            else currentEnteredNumberFlow.value
        } else {
            newValue.toIntOrNull()
        }

        currentEnteredNumberFlow.value = calculatedValue
    }

    private fun launchSummator(value: Int) = launch {
        currentTextFlow.value = ""
        emitNumberFlow.resetReplayCache()

        val flows = createFlows(value)
        // Результирующий Flow должен суммировать значения всех N Flow.
        flows.forEach { flow ->
            launch {
                // В задаче flow эмитит только 1 значение -> take(1)
                flow.take(1).collect { newValue ->
                    val sdfds = getPrevEmittedValue()
                    Log.d("!!", "getPrevEmittedValue: $sdfds")
                    Log.d("!!", "newValue: $newValue")
                    val number = EmitNumber(
                        previousValue = getPrevEmittedValue(),
                        newValue = newValue
                    )
                    emitNumberFlow.emit(number)
                }
            }
        }
    }

    private fun getPrevEmittedValue(): Int =
        emitNumberFlow.replayCache.firstOrNull()?.sum ?: 0

    private fun createFlows(countOfFlowToCreate: Int): Array<Flow<Int>> {
        val flows = mutableListOf<Flow<Int>>()
        // Необходимо создать массив Flow<Int>, количества N
        repeat(countOfFlowToCreate) { index ->
            flows += flow {
                // после задержки в (index + 1) * 100
                val delay = (index + 1) * 100L
                delay(delay)
                // эмитит значение index + 1
                emit(index + 1)
            }
        }
        return flows.toTypedArray()
    }

    private fun observeEmitNumberFlow() = launch {
        emitNumberFlow.collect { newNumber ->
            // Суммирующий Flow должен возвращать значение после обновления каждого из N Flow.
            currentTextFlow.update {
                it + "\n" + newNumber.sum
            }
        }
    }
}