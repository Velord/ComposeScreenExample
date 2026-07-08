package com.velord.core.ui.compose.animation.interpolator

import androidx.compose.animation.core.Easing
import com.velord.core.ui.annotation.ConstructorLikeFunction
import kotlin.math.PI
import kotlin.math.sin

private const val DEFAULT_TENSION = 50f

@ConstructorLikeFunction
fun CircularSpringInterpolatorEasing(
    tension: Float = DEFAULT_TENSION,
): Easing  = CircularSpringInterpolator(tension)

class CircularSpringInterpolator(
    private val tension: Float = DEFAULT_TENSION,
) : Easing {

    override fun transform(fraction: Float): Float {
        val sinusOne = sin(tension * fraction)
        val sinusTwo = sin(PI.toFloat() * fraction)

        return sinusOne * sinusTwo + fraction
    }
}

