package com.velord.core.ui.utils.modifier

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer

object HangingDefaults {

    val shift: HangingShift = HangingShift.Default

    val animation: HangingAnimation = HangingAnimation.Default

    val pivotPoint: HangingPivotPoint = HangingPivotPoint.Top


    fun shift(
        startRotationAngle: Int,
        endRotationAngle: Int = -startRotationAngle
    ): HangingShift = HangingShift(startRotationAngle, endRotationAngle)

    fun animation(
        duration: Int = HangingAnimation.DefaultDuration,
        easing: Easing = LinearEasing,
    ): HangingAnimation = HangingAnimation(duration, easing)

    fun pivot(
        pivotFractionX: Float,
        pivotFractionY: Float
    ): HangingPivotPoint = HangingPivotPoint(pivotFractionX, pivotFractionY)
}

@Immutable
class HangingShift internal constructor(
    internal val leftRotationAngle: Int,
    internal val rightRotationAngle: Int
) {
    companion object {
        val Default = HangingShift(leftRotationAngle = 25, rightRotationAngle = -25)
    }
}

@Immutable
class HangingAnimation internal constructor(
    internal val duration: Int,
    internal val easing: Easing
) {
    companion object {
        internal const val DefaultDuration = 1000

        val Default = HangingAnimation(
            duration = DefaultDuration,
            easing = LinearEasing,
        )
    }
}

@Immutable
class HangingPivotPoint internal constructor(
    internal val pivotFractionX: Float,
    internal val pivotFractionY: Float
) {
    companion object {
        val Top = HangingPivotPoint(pivotFractionX = 0.5f, pivotFractionY = 0f)
        val Bottom = HangingPivotPoint(pivotFractionX = 0.5f, pivotFractionY = 1f)
        val Left = HangingPivotPoint(pivotFractionX = 0f, pivotFractionY = 0.5f)
        val Right = HangingPivotPoint(pivotFractionX = 1f, pivotFractionY = 0.5f)
    }
}

fun Modifier.hanging(
    shift: HangingShift = HangingDefaults.shift,
    animation: HangingAnimation = HangingDefaults.animation,
    pivotPoint: HangingPivotPoint = HangingDefaults.pivotPoint
): Modifier = composed {
    val valueState = rememberInfiniteTransition(label = "hanging").animateFloat(
        initialValue = shift.leftRotationAngle.toFloat(),
        targetValue = shift.rightRotationAngle.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animation.duration,
                easing = animation.easing
            ),
            // Can't be another repeat mode
            // If it's not Reverse - it is not hanging effect
            repeatMode = RepeatMode.Reverse
        ),
        label = "hanging animated value"
    )

    this.then(
        graphicsLayer {
            // Rotation to be at the center at the top
            transformOrigin = TransformOrigin(
                pivotFractionX = pivotPoint.pivotFractionX,
                pivotFractionY = pivotPoint.pivotFractionY
            )
            rotationZ = valueState.value
        }
    )
}