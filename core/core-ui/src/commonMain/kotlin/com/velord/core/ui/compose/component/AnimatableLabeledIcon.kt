package com.velord.core.ui.compose.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnimatableLabeledIcon(
    label: String,
    painter: Painter,
    scale: Float,
    color: Color,
    modifier: Modifier = Modifier,
    iconSize: Dp = 64.dp,
    glowColor: Color? = null,
    contentDescription: String = label,
    animateDuration: Int = 500,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        IconWithGlow(
            painter = painter,
            scale = scale,
            color = color,
            iconSize = iconSize,
            glowColor = glowColor,
            contentDescription = contentDescription,
            animateDuration = animateDuration,
        )
        val animatedColorState: State<Color> = animateColorAsState(
            targetValue = color,
            animationSpec = TweenSpec(
                durationMillis = animateDuration,
                easing = FastOutSlowInEasing,
            ),
            label = "AnimatableLabeledIcon textColor",
        )
        Text(
            text = label,
            color = animatedColorState.value,
            modifier = Modifier.padding(top = 2.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
            ),
        )
    }
}

@Composable
private fun IconWithGlow(
    painter: Painter,
    scale: Float,
    color: Color,
    contentDescription: String,
    modifier: Modifier = Modifier,
    iconSize: Dp = 64.dp,
    glowColor: Color? = null,
    animateDuration: Int = 500,
) {
    val animatedScaleState: State<Float> = animateFloatAsState(
        targetValue = scale,
        animationSpec = TweenSpec(
            durationMillis = animateDuration,
            easing = FastOutSlowInEasing,
        ),
        label = "AnimatableLabeledIcon scale",
    )
    val animatedColorState: State<Color> = animateColorAsState(
        targetValue = color,
        animationSpec = TweenSpec(
            durationMillis = animateDuration,
            easing = FastOutSlowInEasing,
        ),
        label = "AnimatableLabeledIcon color",
    )

    Box(
        modifier = modifier.size(iconSize + 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (glowColor != null) {
            Box(
                modifier = Modifier
                    .size(iconSize + 34.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                glowColor.copy(alpha = 0.9f),
                                glowColor.copy(alpha = 0.0f),
                            ),
                        ),
                    ),
            )
        }
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = animatedColorState.value,
            modifier = Modifier
                .size(iconSize)
                .graphicsLayer {
                    scaleX = animatedScaleState.value
                    scaleY = animatedScaleState.value
                },
        )
    }
}
