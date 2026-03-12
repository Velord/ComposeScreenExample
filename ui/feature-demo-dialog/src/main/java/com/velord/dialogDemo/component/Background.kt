package com.velord.dialogDemo.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput

@Composable
internal fun Background(
    isVisibleState: State<Boolean>,
    animationInProgressState: State<Boolean>,
    color: Color,
    animation: DialogPredefinedAnimation,
    modifier: Modifier,
    onDismissRequest: () -> Unit
) {
    AnimatedVisibility(
        visible = animationInProgressState.value && isVisibleState.value,
        enter = animation.enter,
        exit = animation.exit
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit) { detectTapGestures { onDismissRequest() } }
                .background(color)
        )
    }
}