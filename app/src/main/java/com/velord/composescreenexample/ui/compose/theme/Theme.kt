package com.velord.composescreenexample.ui.compose.theme

import android.app.Activity
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
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
import com.velord.composescreenexample.utils.getActivity

private val DarkColorScheme = darkColorScheme(
    primary = Color.Heliotrope,
    secondary = Color.Rum,
    tertiary = Color.Carnation
)

private val LightColorScheme = lightColorScheme(
    primary = Color.Heliotrope,
    secondary = Color.Rum,
    tertiary = Color.Carnation,
    background = Color.SteelGray,
    surface = Color.GunPowder,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.Charade,
    onSurface = Color.Zumthor,
)

@Composable
fun MainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (view.isInEditMode.not()) {
        SideEffect {
            (view.context.getActivity())?.window?.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color.Transparent)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
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