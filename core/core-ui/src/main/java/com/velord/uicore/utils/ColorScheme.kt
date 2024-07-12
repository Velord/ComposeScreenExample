package com.velord.uicore.utils

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

internal fun ColorScheme.defineScrimAndDarkScrimColorForSystemBar(
    makeTransparent: Boolean = true
): Pair<Int, Int> {
    val scrim = if (makeTransparent) Color.Transparent.toArgb() else this.surface.toArgb()
    val darkScrim = if (makeTransparent) Color.Transparent.toArgb() else scrim.inverseColor()
    return scrim to darkScrim
}