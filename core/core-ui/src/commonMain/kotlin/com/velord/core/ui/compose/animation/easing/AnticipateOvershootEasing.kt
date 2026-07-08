package com.velord.core.ui.compose.animation.easing

import androidx.compose.animation.core.Easing
import com.velord.core.ui.annotation.ConstructorLikeFunction

private const val DEFAULT_TENSION = 2f
private const val DEFAULT_EXTRA_TENSION = 1.5f
private const val HALF_FRACTION = 0.5f
private const val DOUBLE_FRACTION = 2f

@ConstructorLikeFunction
fun AnticipateOvershootInterpolatorEasing(
    tension: Float = DEFAULT_TENSION,
    extraTension: Float = DEFAULT_EXTRA_TENSION,
): Easing = AnticipateOvershootEasing(
    tension = tension,
    extraTension = extraTension,
)

class AnticipateOvershootEasing(
    tension: Float = DEFAULT_TENSION,
    extraTension: Float = DEFAULT_EXTRA_TENSION,
) : Easing {

    private val totalTension = tension * extraTension

    override fun transform(fraction: Float): Float  = if (fraction < HALF_FRACTION) {
        val anticipateCalc = anticipate(
            value = fraction * DOUBLE_FRACTION,
            tension = totalTension,
        )
        HALF_FRACTION * anticipateCalc
    } else {
        val overshootCalc = overshoot(
            value = fraction * DOUBLE_FRACTION - DOUBLE_FRACTION,
            tension = totalTension,
        )
        HALF_FRACTION * (overshootCalc + DOUBLE_FRACTION)
    }

    private fun anticipate(
        value: Float,
        tension: Float,
    ): Float  = value * value * ((tension + 1f) * value - tension)

    private fun overshoot(
        value: Float,
        tension: Float,
    ): Float  = value * value * ((tension + 1f) * value + tension)
}