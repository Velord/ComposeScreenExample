package com.velord.uicore.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.random.Random

private const val INIT_VALUE = -1
private const val THRESHOLD = INIT_VALUE + 1

@Composable
fun <T> Flow<T>.simulateTriggerStateAsSharedFlow(
    initialValue: T? = null,
    before: suspend (T?) -> T? = { initialValue },
    after: suspend (T?) -> T? = before,
): MutableState<T?> {
    val triggerState = remember {
        mutableIntStateOf(INIT_VALUE)
    }
    LaunchedEffect(this) {
        this@simulateTriggerStateAsSharedFlow.collect {
            triggerState.intValue = Random.nextInt(THRESHOLD, Int.MAX_VALUE)
        }
    }

    val actualState: MutableState<T?> = remember {
        mutableStateOf(initialValue)
    }
    LaunchedEffect(triggerState.intValue) {
        if (triggerState.intValue > THRESHOLD) {
            actualState.value = before(actualState.value)
            actualState.value = after(actualState.value)
        }
    }

    return actualState
}

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun Flow<Unit>.simulateVisibleStateAsSharedFlow(
    betweenStateDuration: Long
): MutableState<Boolean?> = this
    .map { false }
    .simulateTriggerStateAsSharedFlow(
    initialValue = false,
    before = { it?.not() },
    after = {
        delay(betweenStateDuration)
        it?.not()
    },
)