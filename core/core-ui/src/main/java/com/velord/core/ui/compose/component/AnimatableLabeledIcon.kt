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
<<<<<<<< HEAD:core-ui/src/main/java/com/velord/uicore/compose/component/AnimatableLabeledIcon.kt
========
import androidx.compose.ui.graphics.graphicsLayer
>>>>>>>> 76f71e457c730912e8e00a3beb96f602e7765555:core/core-ui/src/main/java/com/velord/core/ui/compose/component/AnimatableLabeledIcon.kt
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
<<<<<<<< HEAD:core-ui/src/main/java/com/velord/uicore/compose/component/AnimatableLabeledIcon.kt
    val animatedScale: Float by animateFloatAsState(
        targetValue = scale,
        animationSpec = TweenSpec(
            durationMillis = animateDuration,
            easing = FastOutSlowInEasing
        ),
        label = "AnimatableLabeledIcon scale"
    )
    val animatedColor by animateColorAsState(
        targetValue = color,
        animationSpec = TweenSpec(
            durationMillis = animateDuration,
            easing = FastOutSlowInEasing
        ),
        label = "AnimatableLabeledIcon color"
    )

========
>>>>>>>> 76f71e457c730912e8e00a3beb96f602e7765555:core/core-ui/src/main/java/com/velord/core/ui/compose/component/AnimatableLabeledIcon.kt
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Icon(
            painter = painter,
<<<<<<<< HEAD:core-ui/src/main/java/com/velord/uicore/compose/component/AnimatableLabeledIcon.kt
            contentDescription = null,
            tint = animatedColor,
            modifier = modifier
                .scale(animatedScale)
                .size(iconSize)
========
            scale = scale,
            color = color,
            iconSize = iconSize,
            animateDuration = animateDuration
>>>>>>>> 76f71e457c730912e8e00a3beb96f602e7765555:core/core-ui/src/main/java/com/velord/core/ui/compose/component/AnimatableLabeledIcon.kt
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
    // TODO: reduce recompositions
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
        tint = animatedColorState.value,
        modifier = modifier
            .graphicsLayer {
                scaleX = animatedScaleState.value
                scaleY = animatedScaleState.value
            }
            .size(iconSize)
    )
}