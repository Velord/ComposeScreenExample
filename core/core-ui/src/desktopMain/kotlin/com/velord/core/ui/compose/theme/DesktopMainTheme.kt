package com.velord.core.ui.compose.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
internal actual fun platformDynamicColorScheme(
    useDarkTheme: Boolean,
    dynamicColor: Boolean,
    apiAvailable: Boolean,
): ColorScheme? = null

@Composable
internal actual fun PlatformSystemBarsEffect(
    colorScheme: ColorScheme,
    useDarkTheme: Boolean,
    dynamicColor: Boolean,
) = Unit