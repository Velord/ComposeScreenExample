package com.velord.core.ui.util

import androidx.compose.ui.graphics.Color

private const val LUMINANCE_RED = 0.299
private const val LUMINANCE_GREEN = 0.587
private const val LUMINANCE_BLUE = 0.114
private const val LUMINANCE_THRESHOLD = 0.5
private const val COLOR_MASK = 0x00ffffff

fun Color.toBlackOrWhite(): Color {
    val lum = 1 - ((LUMINANCE_RED * red) + (LUMINANCE_GREEN * green) + (LUMINANCE_BLUE * blue))
    return if (lum < LUMINANCE_THRESHOLD) Color.Black else Color.White
}

fun Int.inverseColor(): Int = this xor COLOR_MASK
