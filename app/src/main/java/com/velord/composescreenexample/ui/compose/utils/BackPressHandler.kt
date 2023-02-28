package com.velord.composescreenexample.ui.compose.utils

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember

@Composable
fun BackPressHandler(
    enabled: Boolean = true,
    onBackPressed: () -> Unit
) {
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
    }

    DisposableEffect(key1 = dispatcher) {
        dispatcher?.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}