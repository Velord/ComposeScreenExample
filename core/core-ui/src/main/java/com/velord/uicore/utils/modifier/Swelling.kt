package com.velord.uicore.utils.modifier

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.swelling(
    initialScale: Float = 1f,
    targetScale: Float = 1.3f,
    duration: Int = 1000,
    easing: Easing = LinearEasing,
    repeatMode: RepeatMode = RepeatMode.Reverse,
): Modifier = composed {
    val valueState = rememberInfiniteTransition(label = "swelling").animateFloat(
        initialValue = initialScale,
        targetValue = targetScale,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                easing = easing
            ),
            repeatMode = repeatMode
        ),
        label = "swelling animated value"
    )

    this.then(
        graphicsLayer {
            scaleX = valueState.value
            scaleY = valueState.value
        }
    )
}