package com.example.windyappflowsummator

import com.velord.util.viewModel.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import java.math.BigInteger

data class EmitNumber(
    val newValue: BigInteger,
    val previousValue: BigInteger
) {
    val sum: BigInteger = newValue + previousValue

    companion object {
        val DEFAULT = EmitNumber(BigInteger.ZERO, BigInteger.ZERO)
    }
}

class FlowSummatorViewModel : BaseViewModel() {

    val currentEnteredNumberFlow: MutableStateFlow<Int?> = MutableStateFlow(null)
    val sumFlow: MutableSharedFlow<EmitNumber> = MutableSharedFlow(
        replay = 1,
        extraBufferCapacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    private val launchSumFlow: MutableSharedFlow<Int> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private var launchSumJob: Job? = null

    init {
        observeLaunchSumFlow()
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

    private fun getPrevEmittedValue(): BigInteger =
        sumFlow.replayCache.firstOrNull()?.sum ?: BigInteger.ZERO

    private suspend fun createFlows(countOfFlowToCreate: Int): Array<Flow<Int>> {
        val flows = mutableListOf<Flow<Int>>()
        // Необходимо создать массив Flow<Int>, количества N
        repeat(countOfFlowToCreate) { index ->
            yield()
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

    private fun observeLaunchSumFlow() = launch(Dispatchers.IO) {
        launchSumFlow
            .onEach { sumFlow.emit(EmitNumber.DEFAULT) }
            .collectLatest { flowCount ->
                launchSumJob?.cancel()
                launchSumJob = createThanLaunchFlowsJob(flowCount) {
                    sumFlow.emit(it)
                }
            }
    }

    private fun CoroutineScope.createThanLaunchFlowsJob(
        flowCount: Int,
        onEmit: suspend (EmitNumber) -> Unit
    ): Job = launch {
        val flows = createFlows(flowCount)
        // Результирующий Flow должен суммировать значения всех N Flow.
        flows.forEach { flow ->
            launch {
                flow.collect { newNumber ->
                    ensureActive()
                    val number = EmitNumber(
                        previousValue = getPrevEmittedValue(),
                        newValue = newNumber.toBigInteger()
                    )
                    onEmit(number)
                }
            }
        }
    }

    companion object {
        // Суммирующий Flow должен возвращать значение после обновления каждого из N Flow.
        fun MutableSharedFlow<EmitNumber>.mapToCumulativeStringEachNumberByLine(): Flow<String> {
            var cumulativeStr = StringBuilder("")
            return map {
                if (it == EmitNumber.DEFAULT) cumulativeStr = StringBuilder("")
                // Каждое обновление должно находиться на новой строчке.
                else cumulativeStr.append("\n" + it.sum)

                cumulativeStr.toString()
            }.buffer(Int.MAX_VALUE).flowOn(Dispatchers.IO)
        }
    }
}