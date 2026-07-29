package com.velord.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.velord.core.ui.compose.theme.MainTheme
import com.velord.core.ui.util.LocalTheme
import com.velord.model.setting.AppThemeConfig
import com.velord.ui.sharedviewmodel.ThemeUiState
import com.velord.ui.sharedviewmodel.ThemeVM
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppThemeHost(
    themeVM: ThemeVM = koinViewModel(),
    content: @Composable () -> Unit,
) {
    val themeState: State<ThemeUiState?> = themeVM.uiStateFlow.collectAsStateWithLifecycle()
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
            specialTheme = localThemeConfig.config.current,
            content = content,
        )
    }
}
