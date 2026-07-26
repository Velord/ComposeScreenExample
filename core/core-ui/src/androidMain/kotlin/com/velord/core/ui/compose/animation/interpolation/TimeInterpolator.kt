package com.velord.core.ui.compose.animation.interpolation

import android.animation.TimeInterpolator
import androidx.compose.animation.core.Easing

fun TimeInterpolator.toEasing() = Easing { x -> getInterpolation(x) }
