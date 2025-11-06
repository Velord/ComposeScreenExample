package com.velord.uicore.compose.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.velord.uicore.compose.shape.ArcAtBottomCenterShape

@Composable
fun PervasiveArcFromBottomLayout(
    isVisible: Boolean,
    abortAnimationOnEdge: Boolean,
    shape: Shape = CutCornerShape(10.dp),
    animationDuration: Int = 1000,
    content: @Composable (currentShape: Shape, defaultShape: Shape) -> Unit = { _, _ -> },
) {
    val target = if (isVisible) 1f else 0f
    val duration = if (isVisible) animationDuration else 0
    val animationProgressState by animateFloatAsState(
        targetValue = target,
        animationSpec = tween(durationMillis = duration, easing = LinearEasing),
        label = ""
    )
    val isEdgeState = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = isVisible) {
        if (isVisible.not()) {
            isEdgeState.value = false
        }
    }

    val currentShape: Shape = when {
        isEdgeState.value -> shape
        animationProgressState == 1f -> shape
        else -> {
            val progress = (animationProgressState * 100).toInt()
            ArcAtBottomCenterShape(progress) {
                if (abortAnimationOnEdge) {
                    isEdgeState.value = true
                }
            }
        }
    }

    if (isVisible) {
        content(currentShape, shape)
    }
}