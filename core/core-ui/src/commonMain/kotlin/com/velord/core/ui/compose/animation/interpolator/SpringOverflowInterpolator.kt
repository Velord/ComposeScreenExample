@file:Suppress("MagicNumber")

package com.velord.core.ui.compose.animation.interpolator

import androidx.compose.animation.core.Easing
import com.velord.core.ui.annotation.ConstructorLikeFunction
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sin

private const val DEFAULT_STIFFNESS = 0.35f
private const val DECAY_FACTOR = 10
private const val STIFFNESS_DIVISOR = 4

@ConstructorLikeFunction
fun SpringOverflowInterpolatorEasing(
    stiffness: Float = DEFAULT_STIFFNESS,
): Easing = SpringOverflowInterpolator(stiffness)

class SpringOverflowInterpolator(
    private val stiffness: Float = DEFAULT_STIFFNESS,
) : Easing {

    override fun transform(fraction: Float): Float {
        val twoPowTen = 2.0.pow((-DECAY_FACTOR * fraction).toDouble())
        val stiffnessByInput = fraction - stiffness / STIFFNESS_DIVISOR
        val doublePi = 2 * PI
        val sinus = sin(stiffnessByInput * doublePi / stiffness)

        return (twoPowTen * sinus + 1).toFloat()
    }
}

