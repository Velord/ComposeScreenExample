package com.velord.composescreenexample.ui.compose.component

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val DELAY_TO_EXIT_APP = 3000L
private const val DEFAULT_TIME = 0L

@Composable
fun OnBackPressHandler(
    duration: Long = DELAY_TO_EXIT_APP,
    onEdgeViolation: () -> Unit = {},
    afterEdge: () -> Unit = {},
    onClick: suspend () -> Unit = {},
) {
    val currentOnEdgeViolation by rememberUpdatedState(onEdgeViolation)
    val currentAfterEdge by rememberUpdatedState(afterEdge)
    val currentOnClick by rememberUpdatedState(onClick)

    val timeShapshotState = remember {
        mutableStateOf(DEFAULT_TIME)
    }
    val triggerState = remember {
        mutableStateOf(DEFAULT_TIME)
    }

    BackHandler {
        triggerState.value = System.currentTimeMillis()
    }

    LaunchedEffect(key1 = triggerState.value) {
        if (triggerState.value == DEFAULT_TIME) return@LaunchedEffect

        val isEdgeViolated = (timeShapshotState.value + duration) > System.currentTimeMillis()
        if (isEdgeViolated) currentOnEdgeViolation()

        launch {
            currentOnClick()
        }
        timeShapshotState.value = System.currentTimeMillis()
        delay(duration)
        currentAfterEdge()
    }
}

@Composable
fun SnackBarOnBackPressHandler(
    message: String,
    modifier: Modifier = Modifier,
    duration: Long = DELAY_TO_EXIT_APP,
    onBackClickLessThanDuration: () -> Unit = {},
    content: @Composable (SnackbarData) -> Unit,
) {
    val snackbarHostState = remember {
        mutableStateOf(SnackbarHostState())
    }

    OnBackPressHandler(
        duration = duration,
        onEdgeViolation = onBackClickLessThanDuration,
        afterEdge = {
            snackbarHostState.value.currentSnackbarData?.dismiss()
        },
        onClick = {
            snackbarHostState.value.showSnackbar(
                message = message,
                duration = SnackbarDuration.Indefinite,
            )
        }
    )

    SnackbarHost(
        hostState = snackbarHostState.value,
        modifier = modifier,
        snackbar = content
    )
}