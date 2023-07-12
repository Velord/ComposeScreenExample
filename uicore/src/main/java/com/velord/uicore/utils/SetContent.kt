package com.velord.uicore.utils

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.velord.uicore.compose.theme.MainTheme
import com.velord.util.context.getActivity

fun ComponentActivity.setContentWithTheme(
    screen: @Composable ComposeView.() -> Unit
): ComposeView = ComposeView(this).setContentWithTheme(screen)

fun Fragment.setContentWithTheme(
    screen: @Composable ComposeView.() -> Unit
): ComposeView = ComposeView(requireContext()).setContentWithTheme(screen)

context(LifecycleOwner)
@SuppressLint("UnrememberedMutableState")
fun ComposeView.setContentWithTheme(
    screen: @Composable ComposeView.() -> Unit
): ComposeView = apply {
    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
    setContent {
        val activity = LocalContext.current.getActivity()
        val settingsViewModel = viewModel<SettingsViewModel>(
            viewModelStoreOwner = activity as ViewModelStoreOwner
        )
        val themeSwitcherState: State<ThemeSwitcher> = settingsViewModel.themeSwitcherFlow
            .collectAsStateWithLifecycle(ThemeSwitcher.considerSystem())

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