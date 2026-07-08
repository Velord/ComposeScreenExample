package com.velord.core.ui.compose.theme

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.velord.core.ui.util.color.defineScrimAndDarkScrimColorForSystemBar

@Composable
internal actual fun platformDynamicColorScheme(
    useDarkTheme: Boolean,
    dynamicColor: Boolean,
    apiAvailable: Boolean,
): ColorScheme? {
    if (dynamicColor.not() || apiAvailable.not()) return null

    val context = LocalContext.current

    return if (useDarkTheme) {
        dynamicDarkColorScheme(context)
    } else {
        dynamicLightColorScheme(context)
    }
}

@Composable
internal actual fun PlatformSystemBarsEffect(
    colorScheme: ColorScheme,
    useDarkTheme: Boolean,
    dynamicColor: Boolean,
) {
    val activity = LocalActivity.current as? ComponentActivity ?: return

    LaunchedEffect(
        activity,
        colorScheme,
        useDarkTheme,
        dynamicColor,
    ) {
        val colors = colorScheme.defineScrimAndDarkScrimColorForSystemBar(makeTransparent = true)

        val light = SystemBarStyle.light(
            scrim = colors.scrim.toArgb(),
            darkScrim = colors.darkScrim.toArgb(),
        )
        val dark = SystemBarStyle.dark(scrim = colors.scrim.toArgb())
        val statusBarStyle = if (useDarkTheme) dark else light

        activity.enableEdgeToEdge(
            statusBarStyle = statusBarStyle,
            navigationBarStyle = statusBarStyle,
        )
    }
}