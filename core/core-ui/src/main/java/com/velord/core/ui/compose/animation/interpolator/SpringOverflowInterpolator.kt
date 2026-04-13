@file:Suppress("MagicNumber")

package com.velord.core.ui.compose.animation.interpolator

import android.view.animation.Interpolator
import androidx.compose.animation.core.Easing
import com.velord.core.ui.annotation.ConstructorLikeFunction
import kotlin.math.pow
import kotlin.math.sin

@ConstructorLikeFunction
fun SpringOverflowInterpolatorEasing(stiffness: Float = 0.35f): Easing =
    SpringOverflowInterpolator(stiffness).toEasing()

class SpringOverflowInterpolator(
    private val stiffness: Float = 0.35f
) : Interpolator {

    override fun getInterpolation(input: Float): Float {
        val twoPowTen = 2.0.pow((-DECAY_FACTOR * input).toDouble())
        val stiffnessByInput = (input - stiffness / STIFFNESS_DIVISOR)
        val doublePi = 2 * Math.PI
        val sinus = sin(stiffnessByInput * doublePi / stiffness)
        return (twoPowTen * sinus + 1).toFloat()
    }

    companion object {
        private const val DECAY_FACTOR = 10
        private const val STIFFNESS_DIVISOR = 4
    }
}
