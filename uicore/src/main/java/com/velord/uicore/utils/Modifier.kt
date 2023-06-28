package com.velord.uicore.utils

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.velord.uicore.compose.component.LinearGradientShaderCanvas

fun Modifier.drawBehindLinearGradientShaderCanvas(
    animatedValue: Float,
    startColor: Color = Color.Transparent,
    centerColor: Color = Color.White,
    endColor: Color = Color.Transparent,
    gradientColorAndPosition: List<Pair<Color, Float>> = listOf(
        startColor to 0f,
        centerColor to animatedValue,
        endColor to 1f
    )
) = this.then(
    Modifier.drawBehind {
        LinearGradientShaderCanvas(
            animatedValue = animatedValue,
            startColor = startColor,
            centerColor = centerColor,
            endColor = endColor,
            gradientColorAndPosition = gradientColorAndPosition
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
    gradientColorAndPosition: @Composable (animatedValue: Float) -> List<Pair<Color, Float>> = {
        listOf(
            startColor to 0f,
            centerColor to it,
            endColor to 1f
        )
    },
    reverse: Boolean = false
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmering")
    val animatedValueState = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = duration, easing = easing),
            repeatMode = repeatMode
        ),
        label = "shimmering animated value"
    )

    val positionsBasedOnValue = gradientColorAndPosition(animatedValueState.value)
    val orReverse = if (reverse) {
        // After map 0f, 0.2f, 1.f -> 1f, 0.8f, 0f
        // Reverse the list is necessary
        // Because the gradient first color can not be 1f
        positionsBasedOnValue
            .map { (color, position) -> color to 1f - position }
            .reversed()
    } else {
        positionsBasedOnValue
    }

    this.drawBehindLinearGradientShaderCanvas(
        animatedValue = animatedValueState.value,
        startColor = startColor,
        centerColor = centerColor,
        endColor = endColor,
        gradientColorAndPosition = orReverse
    )
}

fun Modifier.blinkingShadow(
    elevationMax: Dp,
    shape: Shape,
    elevationMin: Dp = 1.dp,
    duration: Int = 1000,
    repeatMode: RepeatMode = RepeatMode.Reverse,
    animationSpec: InfiniteRepeatableSpec<Float> = infiniteRepeatable(
        animation = tween(durationMillis = duration),
        repeatMode = repeatMode
    ),
    ambientColor: Color = Color.Black,
    spotColor: Color = Color.Black,
): Modifier = composed {
    val valueState = rememberInfiniteTransition(label = "blinkingShadow transition").animateFloat(
        initialValue = elevationMin.value,
        targetValue = elevationMax.value,
        animationSpec = animationSpec,
        label = "blinkingShadow float"
    )

    this.then(
        Modifier.shadow(
            elevation = valueState.value.dp,
            shape = shape,
            ambientColor = ambientColor,
            spotColor = spotColor
        )
    )
}