package com.velord.core.ui.util

import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.velord.core.ui.compose.theme.MainTheme
import com.velord.infrastructure.util.context.getActivity
import com.velord.model.setting.AppThemeConfig
import com.velord.ui.sharedviewmodel.ThemeUiState
import com.velord.ui.sharedviewmodel.ThemeViewModel

fun ComponentActivity.setContentWithTheme(
    screen: @Composable ComposeView.() -> Unit
): ComposeView = ComposeView(this).setContentWithTheme(screen)

fun Fragment.setContentWithTheme(
    screen: @Composable ComposeView.() -> Unit
): ComposeView = ComposeView(requireContext()).setContentWithTheme(screen)

context(_: LifecycleOwner)
fun ComposeView.setContentWithTheme(
    screen: @Composable ComposeView.() -> Unit
): ComposeView = apply {
    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
    setContent {
        val activity = LocalContext.current.getActivity()
        val themeViewModel = viewModel<ThemeViewModel>(activity as ViewModelStoreOwner)
        val themeState: State<ThemeUiState?> = themeViewModel.uiStateFlow.collectAsStateWithLifecycle()

        val theme = themeState.value?.appThemeConfig ?: AppThemeConfig.DEFAULT
        CompositionLocalProvider(LocalTheme provides theme) {
            val localThemeConfig = LocalTheme.current
            val isDark = if (localThemeConfig.config.abideToOs) {
                isSystemInDarkTheme()
            } else {
                localThemeConfig.config.useDarkTheme
            }
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

