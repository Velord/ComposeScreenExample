package com.velord.core.ui.compose.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnimatableLabeledIcon(
    label: String,
    painter: Painter,
    scale: Float,
    color: Color,
    modifier: Modifier = Modifier,
    iconSize: Dp = 64.dp,
    animateDuration: Int = 500,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // Delegates to the private optimized Icon below
        Icon(
            painter = painter,
            scale = scale,
            color = color,
            iconSize = iconSize,
            animateDuration = animateDuration
        )
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun Icon(
    painter: Painter,
    scale: Float,
    color: Color,
    modifier: Modifier = Modifier,
    iconSize: Dp = 64.dp,
    animateDuration: Int = 500,
) {
    val animatedScaleState: State<Float> = animateFloatAsState(
        targetValue = scale,
        animationSpec = TweenSpec(
            durationMillis = animateDuration,
            easing = FastOutSlowInEasing
        ),
        label = "AnimatableLabeledIcon scale"
    )

    val animatedColorState: State<Color> = animateColorAsState(
        targetValue = color,
        animationSpec = TweenSpec(
            durationMillis = animateDuration,
            easing = FastOutSlowInEasing
        ),
        label = "AnimatableLabeledIcon color"
    )

    Icon(
        painter = painter,
        contentDescription = null,
        tint = animatedColorState.value, // This will only cause this specific Icon to recompose, not the text!
        modifier = modifier
            .size(iconSize)
            .graphicsLayer {
                // Reading state inside graphicsLayer skips recomposition and layout phases
                scaleX = animatedScaleState.value
                scaleY = animatedScaleState.value
            }
    )
}