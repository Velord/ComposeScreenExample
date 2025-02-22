package com.velord.flowsummator

import com.velord.sharedviewmodel.CoroutineScopeViewModel
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigInteger

data class FlowSummatorUiState(
    val currentEnteredNumber: Int?,
    val sum: String
) {
    companion object {
        val DEFAULT = FlowSummatorUiState(null, "")
    }
}

sealed interface FlowSummatorUiAction {
    data object StartClick : FlowSummatorUiAction
    data class NewEnteredValue(val newValue: String) : FlowSummatorUiAction
}

class FlowSummatorViewModel : CoroutineScopeViewModel() {

    val uiState = MutableStateFlow(FlowSummatorUiState.DEFAULT)
    private val actionFlow = MutableSharedFlow<FlowSummatorUiAction>()

    private val sumFlow: MutableSharedFlow<BigInteger> = MutableSharedFlow(
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
        observe()
    }

    fun onAction(action: FlowSummatorUiAction) {
        launch {
            actionFlow.emit(action)
        }
    }

    private fun onStartClick() = launch {
        // Case when user didn't enter anything
        val enteredNumber = uiState.value.currentEnteredNumber ?: return@launch
        launchSumFlow.emit(enteredNumber)
    }

    private fun onNewEnteredValue(newValue: String) {
        val calculatedValue = if (newValue.toIntOrNull() == null) {
            // Case when user entered not a number(from clipboard framework)
            // or empty string
            // or that is not a number(Int in that case)
            if (newValue.isEmpty()) null // Case when user cleared the field
            else uiState.value.currentEnteredNumber
        } else {
            newValue.toIntOrNull()
        }

        uiState.update {
            it.copy(currentEnteredNumber = calculatedValue)
        }
    }

    private fun getPrevEmittedValue(): BigInteger =
        sumFlow.replayCache.firstOrNull() ?: BigInteger.ZERO

    private fun observe() {
        launch {
            actionFlow.collectLatest { action ->
                when (action) {
                    is FlowSummatorUiAction.StartClick -> onStartClick()
                    is FlowSummatorUiAction.NewEnteredValue -> onNewEnteredValue(action.newValue)
                }
            }
        }
        launch(Dispatchers.IO) {
            launchSumFlow
                .onEach { sumFlow.emit(BigInteger.ZERO) }
                .collectLatest { flowCount ->
                    launchSumJob?.cancel()
                    // During cancellation some items can be emitted, need to clear them
                    sumFlow.emit(BigInteger.ZERO)
                    launchSumJob = FlowCreator(
                        countOfFlowToCreate = flowCount,
                        parallelism = true,
                        onEmit = {
                            // The summing Flow must return a value after updating each of the N Flows
                            val newValue = getPrevEmittedValue() + it.toBigInteger()
                            sumFlow.emit(newValue)
                        },
                    ).start(this)
                }
        }
        launch {
            sumFlow.mapToCumulativeStringEachNumberByLine().collectLatest {
                uiState.update { state ->
                    state.copy(sum = it)
                }
            }
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