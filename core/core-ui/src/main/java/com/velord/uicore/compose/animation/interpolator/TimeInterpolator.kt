package com.velord.uicore.compose.animation.interpolator

import android.animation.TimeInterpolator
import androidx.compose.animation.core.Easing

fun TimeInterpolator.toEasing() = Easing { x -> getInterpolation(x) }