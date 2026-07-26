package com.velord.core.ui.util.color

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

internal data class SystemBarScrimColors(
    val scrim: Color,
    val darkScrim: Color,
)

internal fun ColorScheme.defineScrimAndDarkScrimColorForSystemBar(
    makeTransparent: Boolean = true
): SystemBarScrimColors {
    val scrim = if (makeTransparent) Color.Transparent else this.surface
    val darkScrim = if (makeTransparent) Color.Transparent else scrim.inverseColor()
    return SystemBarScrimColors(scrim, darkScrim)
}
