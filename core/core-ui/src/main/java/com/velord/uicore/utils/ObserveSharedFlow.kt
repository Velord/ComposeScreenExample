package com.velord.uicore.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun <T> ObserveSharedFlow(
    flow: MutableSharedFlow<T>,
    onEvent: (T) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val navigationEvents = remember(flow, lifecycleOwner) {
        flow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
    }

    LaunchedEffect(navigationEvents) {
        navigationEvents.collect {
            onEvent(it)
        }
    }
}