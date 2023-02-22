package com.velord.composescreenexample.ui.compose.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.velord.composescreenexample.utils.context.getActivity

private val DarkColorScheme = darkColorScheme(
    primary = Color.DarkPrimary,
    onPrimary = Color.DarkOnPrimary,
    secondary = Color.DarkSecondary,
    onSecondary = Color.DarkOnSecondary,
    tertiary = Color.DarkTertiary,
    onTertiary = Color.DarkOnTertiary,
    background = Color.DarkBackground,
    onBackground = Color.DarkOnBackground,
    surface = Color.DarkSurface,
    onSurface = Color.DarkOnSurface,
    primaryContainer = Color.DarkPrimaryContainer,
    onPrimaryContainer = Color.DarkOnPrimaryContainer,
    secondaryContainer = Color.DarkSecondaryContainer,
    onSecondaryContainer = Color.DarkOnSecondaryContainer,
    tertiaryContainer = Color.DarkTertiaryContainer,
    onTertiaryContainer = Color.DarkOnTertiaryContainer,
    surfaceVariant = Color.DarkSurfaceVariant,
    onSurfaceVariant = Color.DarkOnSurfaceVariant,
    outline = Color.DarkOutline,
)

private val LightColorScheme = lightColorScheme(
    primary = Color.DarkPrimary,
    onPrimary = Color.DarkOnPrimary,
    secondary = Color.DarkSecondary,
    onSecondary = Color.DarkOnSecondary,
    tertiary = Color.DarkTertiary,
    onTertiary = Color.DarkOnTertiary,
    background = Color.DarkBackground,
    onBackground = Color.DarkOnBackground,
    surface = Color.DarkSurface,
    onSurface = Color.DarkOnSurface,
    primaryContainer = Color.DarkPrimaryContainer,
    onPrimaryContainer = Color.DarkOnPrimaryContainer,
    secondaryContainer = Color.DarkSecondaryContainer,
    onSecondaryContainer = Color.DarkOnSecondaryContainer,
    tertiaryContainer = Color.DarkTertiaryContainer,
    onTertiaryContainer = Color.DarkOnTertiaryContainer,
    surfaceVariant = Color.DarkSurfaceVariant,
    onSurfaceVariant = Color.DarkOnSurfaceVariant,
    outline = Color.DarkOutline,
)

@Composable
fun MainTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        useDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (view.isInEditMode.not()) {
        SideEffect {
            (view.context.getActivity())?.window?.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = useDarkTheme
        }
    }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color.Transparent)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = MainShapes,
        typography = MainTypography,
        content = content
    )
}

fun Activity.setContentWithTheme(
    screen: @Composable ComposeView.() -> Unit
): ComposeView = ComposeView(this).setContentWithTheme(screen)

context(Activity)
fun ComposeView.setContentWithTheme(
    screen: @Composable ComposeView.() -> Unit
): ComposeView = apply {
    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
    setContent {
        MainTheme {
            screen()
        }
    }
}

fun Fragment.setContentWithTheme(
    screen: @Composable ComposeView.() -> Unit
): ComposeView = ComposeView(requireContext()).setContentWithTheme(screen)

context(Fragment)
fun ComposeView.setContentWithTheme(
    screen: @Composable ComposeView.() -> Unit
): ComposeView = apply {
    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
    setContent {
        MainTheme {
            screen()
        }
    }
}