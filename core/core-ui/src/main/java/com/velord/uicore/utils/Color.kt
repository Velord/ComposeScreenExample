package com.velord.uicore.utils

import androidx.compose.ui.graphics.Color

fun Color.toBlackOrWhite(): Color {
    val r = this.red
    val g = this.green
    val b = this.blue

    val lum = 1 - ((0.299 * r) + (0.587 * g) + (0.114 * b))

    return if (lum < 0.5) Color.Black else Color.White
}

fun Int.inverseColor(): Int = this xor 0x00ffffff