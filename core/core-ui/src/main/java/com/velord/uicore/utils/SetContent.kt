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
import com.velord.sharedviewmodel.ThemeViewModel
import com.velord.uicore.compose.theme.MainTheme
import com.velord.util.context.getActivity
import com.velord.util.settings.AndroidThemeConfig

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
        val themeViewModel = viewModel<ThemeViewModel>(activity as ViewModelStoreOwner)
        val themeState: State<AndroidThemeConfig?> = themeViewModel.themeFlow
            .collectAsStateWithLifecycle()

        val theme = themeState.value ?: AndroidThemeConfig.considerSystem()
        CompositionLocalProvider(LocalTheme provides theme) {
            val localThemeConfig = LocalTheme.current
            val isDark = if (localThemeConfig.config.abideToOs) {
                isSystemInDarkTheme()
            } else {
                localThemeConfig.config.useDarkTheme
            }
            Log.d("@@@", "config: ${theme}")
            MainTheme(
                abideToOsTheme = localThemeConfig.config.abideToOs,
                useDarkTheme = isDark,
                dynamicColor = localThemeConfig.config.useDynamicColor,
                specialTheme = localThemeConfig.config.current
            ) {
                screen()
            }
        }
    }
}

@Composable
private fun AndroidThemeConfig.Companion.considerSystem(): AndroidThemeConfig = DEFAULT

val LocalTheme = staticCompositionLocalOf { AndroidThemeConfig.DEFAULT }