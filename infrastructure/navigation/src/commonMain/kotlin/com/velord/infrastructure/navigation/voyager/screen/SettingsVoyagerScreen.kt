package com.velord.infrastructure.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationVM
import com.velord.ui.feature.setting.SettingScreen
import com.velord.ui.sharedviewmodel.LanguageVM
import com.velord.ui.sharedviewmodel.ThemeVM
import org.koin.compose.viewmodel.koinViewModel

internal object SettingsVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val themeViewModel = koinViewModel<ThemeVM>()
        val languageViewModel = koinViewModel<LanguageVM>()
        val bottomNavVM = koinViewModel<BottomNavigationVM>()
        SettingScreen(
            themeViewModel = themeViewModel,
            languageViewModel = languageViewModel,
            onGraphCompleted = {
                bottomNavVM.onAction(BottomNavigationUiAction.GraphCompletedHandling)
            },
            onBackClick = {
                bottomNavVM.onAction(BottomNavigationUiAction.BackRequest)
            },
        )
    }
}
