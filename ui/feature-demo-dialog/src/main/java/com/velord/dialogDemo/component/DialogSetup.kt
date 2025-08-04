package com.velord.dialogDemo.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DialogSetup(
    isVisibleState: State<Boolean>,
    action: DialogActions,
    modifier: DialogModifiers,
    animation: DialogAnimations,
    color: DialogColors,
    onDispose: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val animationInProgressState = remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            animationInProgressState.value = true
        }

        Background(
            isVisibleState = isVisibleState,
            animationInProgressState = animationInProgressState,
            color = color.background,
            animation = animation.background,
            modifier = modifier.background,
            onDismissRequest = action.onDismissRequest
        )

        BasicAlertDialog(onDismissRequest = action.onDismissRequest) {
            val window = (LocalView.current.parent as? DialogWindowProvider)?.window

            SideEffect {
                window?.apply {
                    setDimAmount(0f)
                    setWindowAnimations(-1)
                }
            }

            MainBoxVisibility(
                isVisibleState = isVisibleState,
                animationInProgressState = animationInProgressState,
                modifier = modifier.mainBox,
                animation = animation.mainBox,
                color = color.mainBox,
                onDispose = onDispose,
            ) {
                content()
            }
        }
    }
}

@Composable
private fun MainBoxVisibility(
    isVisibleState: State<Boolean>,
    animationInProgressState: State<Boolean>,
    modifier: Modifier,
    animation: DialogPredefinedAnimation,
    color: Color,
    onDispose: () -> Unit,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = animationInProgressState.value && isVisibleState.value,
        enter = animation.enter,
        exit = animation.exit
    ) {
        Box(
            modifier = modifier.background(color),
            contentAlignment = Alignment.Center
        ) {
            content()
        }

        DisposableEffect(Unit) {
            onDispose {
                onDispose()
            }
        }
    }
}