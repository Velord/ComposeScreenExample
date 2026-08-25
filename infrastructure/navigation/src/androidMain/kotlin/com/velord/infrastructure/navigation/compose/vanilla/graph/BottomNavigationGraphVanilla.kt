package com.velord.infrastructure.navigation.compose.vanilla.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.velord.infrastructure.navigation.compose.vanilla.GraphVanilla
import com.velord.infrastructure.navigation.compose.vanilla.navigator.BottomNavigatorVanilla
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationVM
import com.velord.ui.feature.setting.SettingScreen
import com.velord.ui.sharedviewmodel.LanguageVM
import com.velord.ui.sharedviewmodel.ThemeVM
import org.koin.compose.viewmodel.koinViewModel

internal fun NavGraphBuilder.setupBottomNavigationGraphVanilla(
    navigator: BottomNavigatorVanilla,
) {
    setupCameraRecordingGraphVanilla(navigator)
    setupDemoGraphVanilla(navigator)

    composable<GraphVanilla.BottomTab.SettingDestinationVanilla> {
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
