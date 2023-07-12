package com.velord.uicore.utils

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.velord.uicore.compose.theme.MainTheme
import kotlinx.coroutines.flow.Flow

fun ComponentActivity.setContentWithTheme(
    themeSwitcher: Flow<ThemeSwitcher>? = null,
    screen: @Composable ComposeView.() -> Unit
): ComposeView = ComposeView(this).setContentWithTheme(
    themeSwitcher = themeSwitcher,
    screen = screen
)

fun Fragment.setContentWithTheme(
    themeSwitcher: Flow<ThemeSwitcher>? = null,
    screen: @Composable ComposeView.() -> Unit
): ComposeView = ComposeView(requireContext()).setContentWithTheme(
    themeSwitcher = themeSwitcher,
    screen = screen
)

context(LifecycleOwner)
@SuppressLint("UnrememberedMutableState")
fun ComposeView.setContentWithTheme(
    themeSwitcher: Flow<ThemeSwitcher>? = null,
    screen: @Composable ComposeView.() -> Unit
): ComposeView = apply {
    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
    setContent {
        val themeSwitcherState: State<ThemeSwitcher> = themeSwitcher
            ?.collectAsStateWithLifecycle(ThemeSwitcher.considerSystem())
            ?: mutableStateOf(ThemeSwitcher.considerSystem())
        Log.d("@@@", "setContentWithTheme: ${themeSwitcherState.value}")

        CompositionLocalProvider(LocalThemeSwitcher provides themeSwitcherState.value) {
            val localThemeSwitcher = LocalThemeSwitcher.current
            MainTheme(
                useDarkTheme = localThemeSwitcher.useDarkTheme,
                dynamicColor = localThemeSwitcher.useDynamicColor
            ) {
                screen()
            }
        }
    }
}

data class ThemeSwitcher(
    val useDarkTheme: Boolean,
    val useDynamicColor: Boolean
) {
    companion object {
        val DEFAULT = ThemeSwitcher(
            useDarkTheme = false,
            useDynamicColor = false
        )

        @Composable
        fun considerSystem(): ThemeSwitcher = ThemeSwitcher(
            useDarkTheme = isSystemInDarkTheme(),
            useDynamicColor = false
        )
    }
}

val LocalThemeSwitcher = staticCompositionLocalOf { ThemeSwitcher.DEFAULT }