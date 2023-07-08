package com.example.windyappflowsummator

import com.velord.util.viewModel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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

private const val SPLIT_FLOW_CREATION_BY_CHUNK = 10000

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

    private fun observeLaunchSumFlow() = launch(Dispatchers.IO) {
        launchSumFlow
            .onEach { sumFlow.emit(EmitNumber.DEFAULT) }
            .collectLatest { flowCount ->
                launchSumJob?.cancel()
                sumFlow.emit(EmitNumber.DEFAULT)
                launchSumJob = FlowSummator(
                    countOfFlowToCreate = flowCount,
                    splitCreatingBy = SPLIT_FLOW_CREATION_BY_CHUNK,
                    paralellism = true,
                    onEmit = { sumFlow.emit(it) },
                    getLastCachedValue = ::getPrevEmittedValue
                ).start(this)
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