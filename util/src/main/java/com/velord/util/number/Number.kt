package com.velord.util.number

import android.util.DisplayMetrics
import kotlin.math.roundToInt

fun Number.toDp(displayMetrics: DisplayMetrics): Float =
    this.toFloat() / displayMetrics.density

fun Number.toSp(displayMetrics: DisplayMetrics): Float =
    this.toFloat() / displayMetrics.scaledDensity

fun Number.spToPx(displayMetrics: DisplayMetrics): Int =
    (this.toFloat() * displayMetrics.scaledDensity).roundToInt()

fun Number.dpToPx(displayMetrics: DisplayMetrics): Int =
    (this.toFloat() * displayMetrics.density).roundToInt()