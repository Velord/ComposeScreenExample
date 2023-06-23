package com.velord.uicore.utils

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import com.velord.uicore.compose.component.LinearGradientShaderCanvas

fun Modifier.drawBehindLinearGradientShaderCanvas(
    animatedValue: Float,
    startColor: Color = Color.Transparent,
    centerColor: Color = Color.White,
    endColor: Color = Color.Transparent,
    gradientColors: List<Color> = listOf(
        startColor,
        centerColor,
        endColor,
    ),
    colorsPosition: List<Float> = listOf(0f, animatedValue, 1f)
) = this.then(
    Modifier.drawBehind {
        LinearGradientShaderCanvas(
            animatedValue = animatedValue,
            startColor = startColor,
            centerColor = centerColor,
            endColor = endColor,
            gradientColors = gradientColors,
            colorsPosition = colorsPosition
        )
    }
)

fun Modifier.shimmering(
    duration: Int = 1000,
    easing: Easing = LinearEasing,
    repeatMode: RepeatMode = RepeatMode.Restart,
    startColor: Color = Color.Transparent,
    centerColor: Color = Color.White,
    endColor: Color = Color.Transparent,
    // If you want to use more than 3 colors, you can use this parameter
    gradientColors: List<Color> = listOf(
        startColor,
        centerColor,
        endColor,
    ),
    // If you want to use more than 3 colors, you must declare the position of each color
    colorsPosition: (animatedValue: Float) -> List<Float> = { listOf(0f, it, 1f) }
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "")
    val animatedValueState = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = duration, easing = easing),
            repeatMode = repeatMode
        ),
        label = "ShimmerEffect animated value"
    )
    this.drawBehindLinearGradientShaderCanvas(
        animatedValue = animatedValueState.value,
        startColor = startColor,
        centerColor = centerColor,
        endColor = endColor,
        gradientColors = gradientColors,
        colorsPosition = colorsPosition(animatedValueState.value)
    )
}