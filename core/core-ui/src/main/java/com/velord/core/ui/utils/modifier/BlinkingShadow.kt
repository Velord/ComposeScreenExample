package com.velord.core.ui.utils.modifier

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
    val valueState = rememberInfiniteTransition(label = "blinkingShadow").animateFloat(
        initialValue = elevationMin.value,
        targetValue = elevationMax.value,
        animationSpec = animationSpec,
        label = "blinkingShadow animated value"
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