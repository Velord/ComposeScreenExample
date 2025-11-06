package com.velord.uicore.compose.theme

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.velord.model.settings.AndroidThemeConfig
import com.velord.model.settings.SpecialTheme
import com.velord.uicore.compose.shape.MainShapes
import com.velord.uicore.compose.theme.color.DarkColorScheme
import com.velord.uicore.compose.theme.color.LightColorScheme
import com.velord.uicore.utils.defineScrimAndDarkScrimColorForSystemBar

private fun SpecialTheme.toColorScheme(): ColorScheme = when (this) {
    SpecialTheme.DARK -> DarkColorScheme
    SpecialTheme.LIGHT -> LightColorScheme
}

@Composable
fun MainTheme(
    abideToOsTheme: Boolean = true,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    specialTheme: SpecialTheme,
    content: @Composable () -> Unit
) {
    val apiAvailable = AndroidThemeConfig.isSystemDynamicColorAvailable()
    val colorScheme: ColorScheme = when {
        abideToOsTheme.not() && dynamicColor && apiAvailable -> {
            val context = LocalContext.current
            if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        abideToOsTheme -> if (useDarkTheme) DarkColorScheme else LightColorScheme
        else -> specialTheme.toColorScheme()
    }

    val context = LocalActivity.current as ComponentActivity
    LaunchedEffect(key1 = useDarkTheme, key2 = dynamicColor) {
        val (scrim, darkScrim) = colorScheme
            .defineScrimAndDarkScrimColorForSystemBar(makeTransparent = true)
        val light = SystemBarStyle.light(
            scrim = scrim,
            darkScrim = darkScrim
        )
        val dark = SystemBarStyle.dark(scrim = scrim)
        val statusBarStyle = if (useDarkTheme) dark else light
        context.enableEdgeToEdge(
            statusBarStyle = statusBarStyle,
            navigationBarStyle = statusBarStyle
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = MainShapes,
        typography = MainTypography,
        content = content
    )
}