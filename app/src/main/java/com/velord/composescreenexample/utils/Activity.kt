package com.velord.composescreenexample.utils

import android.app.Activity
import android.util.Log
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.material.snackbar.Snackbar
import com.velord.composescreenexample.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val DELAY_TO_EXIT_APP = 3000L
private const val DEFAULT_TIME = 0L

private var timeSnapshot: Long = 0

context(Activity)
fun View.showSnackbarOrFinish() {
    if (timeSnapshot + DELAY_TO_EXIT_APP > System.currentTimeMillis()) finish()
    else Snackbar.make(this, R.string.press_again_to_exit, Snackbar.LENGTH_SHORT).show()

    timeSnapshot = System.currentTimeMillis()
}


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
        Log.d("@@@", "LaunchedEffect: $isEdgeViolated")
        if (isEdgeViolated) currentOnEdgeViolation()

        launch {
            currentOnClick()
        }
        timeShapshotState.value = System.currentTimeMillis()
        delay(duration)
        Log.d("@@@", "LaunchedEffect onEdge")
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

    Log.d("@@@", "SnackBarOnBackPressHandler")
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