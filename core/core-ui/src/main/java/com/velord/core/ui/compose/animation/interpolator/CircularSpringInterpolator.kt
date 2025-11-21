package com.velord.core.ui.compose.animation.interpolator

import android.view.animation.Interpolator
import androidx.compose.animation.core.Easing
import kotlin.math.sin

fun CircularSpringInterpolatorEasing(tension: Float = 50f): Easing =
    CircularSpringInterpolator(tension).toEasing()

class CircularSpringInterpolator(private val tension: Float = 50f) : Interpolator {
    override fun getInterpolation(input: Float): Float {
        val sinusOne = sin(tension * input)
        val sinusTwo = sin(Math.PI * input)
        return (sinusOne * sinusTwo + input).toFloat()
    }
}