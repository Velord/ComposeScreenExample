package com.velord.core.ui.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import com.velord.core.ui.compose.shape.CutShapes
import com.velord.core.ui.compose.shape.RoundedShapes
import com.velord.core.ui.compose.shape.SquareShapes
import com.velord.core.ui.compose.theme.color.DarkColorScheme
import com.velord.core.ui.compose.theme.color.LightColorScheme
import com.velord.core.ui.compose.theme.color.NegativeLightColorScheme
import com.velord.core.ui.compose.theme.color.OceanColorScheme
import com.velord.model.setting.AppShapeStyle
import com.velord.model.setting.AppThemeConfig
import com.velord.model.setting.SpecialTheme

private fun SpecialTheme.toColorScheme(): ColorScheme = when (this) {
    SpecialTheme.DARK -> DarkColorScheme
    SpecialTheme.LIGHT -> LightColorScheme
    SpecialTheme.NEGATIVE_LIGHT -> NegativeLightColorScheme
    SpecialTheme.OCEAN -> OceanColorScheme
}

private fun getDefaultDarkScheme() = SpecialTheme.DEFAULT_DARK_SCHEME.toColorScheme()
private fun getDefaultLightScheme() = SpecialTheme.DEFAULT_LIGHT_SCHEME.toColorScheme()
private fun defineDefaultColorScheme(
    useDarkTheme: Boolean
): ColorScheme = if (useDarkTheme) getDefaultDarkScheme() else getDefaultLightScheme()

private fun AppShapeStyle.toShapes(): Shapes = when(this) {
    AppShapeStyle.ROUNDED -> RoundedShapes
    AppShapeStyle.CUT -> CutShapes
    AppShapeStyle.SQUARE -> SquareShapes
}

@Composable
fun MainTheme(
    abideToOsTheme: Boolean = true,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    specialTheme: SpecialTheme,
    shapeStyle: AppShapeStyle = AppShapeStyle.ROUNDED,
    content: @Composable () -> Unit,
) {
    val isDark = specialTheme.mode.isDark(useDarkTheme)

    val dynamicColorScheme = platformDynamicColorScheme(
        useDarkTheme = isDark,
        dynamicColor = dynamicColor,
        apiAvailable = AppThemeConfig.DEFAULT.isSystemDynamicColorAvailable,
    )

    val colorScheme: ColorScheme = when {
        abideToOsTheme.not() && dynamicColorScheme != null -> dynamicColorScheme
        abideToOsTheme -> defineDefaultColorScheme(isDark)
        else -> specialTheme.toColorScheme()
    }

    PlatformSystemBarsEffect(
        colorScheme = colorScheme,
        useDarkTheme = useDarkTheme,
        dynamicColor = dynamicColor,
    )

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = shapeStyle.toShapes(),
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