package com.velord.uicore.compose.theme

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.velord.uicore.compose.theme.color.DarkColorScheme
import com.velord.uicore.compose.theme.color.LightColorScheme
import com.velord.uicore.compose.theme.shape.MainShapes
import com.velord.uicore.utils.inverseColor

private fun defineScrimAndDarkScrimColorForSystemBar(
    colorScheme: ColorScheme,
    makeTransparent: Boolean = true
): Pair<Int, Int> {
    val scrim = if (makeTransparent) Color.Transparent.toArgb() else colorScheme.surface.toArgb()
    val darkScrim = if (makeTransparent) Color.Transparent.toArgb() else scrim.inverseColor()
    return scrim to darkScrim
}

@Composable
fun MainTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme: ColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        useDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val context = LocalContext.current as ComponentActivity
    DisposableEffect(key1 = useDarkTheme, key2 = dynamicColor) {
        val (scrim, darkScrim) = defineScrimAndDarkScrimColorForSystemBar(
            colorScheme = colorScheme,
            makeTransparent = true
        )
        val light = SystemBarStyle.light(
            scrim = scrim,
            darkScrim = darkScrim
        )
        val dark = SystemBarStyle.dark(scrim = scrim)
        context.enableEdgeToEdge(
            statusBarStyle = if (useDarkTheme) dark else light,
            navigationBarStyle = if (useDarkTheme) dark else light
        )
        onDispose { }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = MainShapes,
        typography = MainTypography,
        content = content
    )
}