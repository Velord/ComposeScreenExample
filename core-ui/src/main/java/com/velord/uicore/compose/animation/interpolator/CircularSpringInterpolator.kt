package com.velord.uicore.compose.animation.interpolator

import android.animation.TimeInterpolator
import android.view.animation.Interpolator
import androidx.compose.animation.core.Easing
import kotlin.math.sin

fun TimeInterpolator.toEasing() = Easing { x -> getInterpolation(x) }

class CircularSpringInterpolator(private val tension: Float = 50f) : Interpolator {
    override fun getInterpolation(input: Float): Float {
        val sinusOne = sin(tension * input)
        val sinusTwo = sin(Math.PI * input)
        return (sinusOne * sinusTwo + input).toFloat()
    }
}