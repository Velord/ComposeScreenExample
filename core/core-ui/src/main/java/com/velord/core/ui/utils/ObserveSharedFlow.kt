package com.velord.core.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterNotNull

// Do not use with shared flow that has replay cache > 0
@Composable
fun <T> ObserveSharedFlow(
    flow: MutableSharedFlow<T>,
    onEvent: (T) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val eventFlow = remember(flow, lifecycleOwner) {
        flow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
    }

    LaunchedEffect(eventFlow) {
        eventFlow.collect {
            onEvent(it)
        }
    }
}

// Do not use with shared flow that has nullable type
@Composable
fun <T> ObserveSharedFlowAsState(
    flow: MutableSharedFlow<T>,
    onEvent: (T) -> Unit,
) {
    val eventFlow = flow.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(eventFlow) {
        snapshotFlow { eventFlow.value }.filterNotNull().collect {
            onEvent(it)
        }
    }
}