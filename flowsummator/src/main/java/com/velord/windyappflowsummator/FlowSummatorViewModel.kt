package com.velord.windyappflowsummator

import com.example.sharedviewmodel.CoroutineScopeViewModel
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

class FlowSummatorViewModel : CoroutineScopeViewModel() {

    val currentEnteredNumberFlow: MutableStateFlow<Int?> = MutableStateFlow(null)
    val sumFlow: MutableSharedFlow<BigInteger> = MutableSharedFlow(
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
        sumFlow.replayCache.firstOrNull() ?: BigInteger.ZERO

    private fun observeLaunchSumFlow() = launch(Dispatchers.IO) {
        launchSumFlow
            .onEach { sumFlow.emit(BigInteger.ZERO) }
            .collectLatest { flowCount ->
                launchSumJob?.cancel()
                // During cancellation some items can be emitted, need to clear them
                sumFlow.emit(BigInteger.ZERO)
                launchSumJob = FlowCreator(
                    countOfFlowToCreate = flowCount,
                    paralellism = true,
                    onEmit = {
                        // The summing Flow must return a value after updating each of the N Flows
                        val newValue = getPrevEmittedValue() + it.toBigInteger()
                        sumFlow.emit(newValue)
                    },
                ).start(this)
            }
    }

    companion object {
        // The resulting Flow must sum the values of all N Flows
        fun MutableSharedFlow<BigInteger>.mapToCumulativeStringEachNumberByLine(): Flow<String> {
            var cumulativeStr = StringBuilder("")
            return map {
                if (it == BigInteger.ZERO) cumulativeStr = StringBuilder("")
                // Every new update should be on the new line
                else cumulativeStr.append("\n" + it)

                cumulativeStr.toString()
            }.buffer(Int.MAX_VALUE).flowOn(Dispatchers.IO)
        }
    }
}