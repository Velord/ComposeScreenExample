package com.velord.uicore.compose.animation.interpolator

import android.view.animation.Interpolator
import androidx.compose.animation.core.Easing
import kotlin.math.pow
import kotlin.math.sin

fun SpringOverflowInterpolatorEasing(stiffness: Float = 0.35f): Easing =
    SpringOverflowInterpolator(stiffness).toEasing()

class SpringOverflowInterpolator(
    private val stiffness: Float = 0.35f
) : Interpolator {
    override fun getInterpolation(input: Float): Float {
        val twoPowTen = 2.0.pow((-10 * input).toDouble())
        val stiffnessByInput = (input - stiffness / 4)
        val doublePi = 2 * Math.PI
        val sinus = sin(stiffnessByInput * doublePi / stiffness)
        return (twoPowTen * sinus + 1).toFloat()
    }
}