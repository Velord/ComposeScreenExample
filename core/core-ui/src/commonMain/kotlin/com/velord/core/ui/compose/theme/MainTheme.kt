package com.velord.core.ui.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.velord.core.ui.compose.shape.MainShapes
import com.velord.core.ui.compose.theme.color.DarkColorScheme
import com.velord.core.ui.compose.theme.color.LightColorScheme
import com.velord.model.setting.AppThemeConfig
import com.velord.model.setting.SpecialTheme

private fun SpecialTheme.toColorScheme(): ColorScheme = when (this) {
    SpecialTheme.DARK -> DarkColorScheme
    SpecialTheme.LIGHT -> LightColorScheme
}

@Composable
fun MainTheme(
    abideToOsTheme: Boolean = true,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    specialTheme: SpecialTheme,
    content: @Composable () -> Unit,
) {
    val dynamicColorScheme = platformDynamicColorScheme(
        useDarkTheme = useDarkTheme,
        dynamicColor = dynamicColor,
        apiAvailable = AppThemeConfig.DEFAULT.isSystemDynamicColorAvailable,
    )

    val colorScheme: ColorScheme = when {
        abideToOsTheme.not() && dynamicColorScheme != null -> dynamicColorScheme
        abideToOsTheme -> if (useDarkTheme) DarkColorScheme else LightColorScheme
        else -> specialTheme.toColorScheme()
    }

    PlatformSystemBarsEffect(
        colorScheme = colorScheme,
        useDarkTheme = useDarkTheme,
        dynamicColor = dynamicColor,
    )

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = MainShapes,
        typography = MainTypography,
        content = content,
    )
}

@Composable
internal expect fun platformDynamicColorScheme(
    useDarkTheme: Boolean,
    dynamicColor: Boolean,
    apiAvailable: Boolean,
): ColorScheme?

@Composable
internal expect fun PlatformSystemBarsEffect(
    colorScheme: ColorScheme,
    useDarkTheme: Boolean,
    dynamicColor: Boolean,
)