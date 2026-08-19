package com.velord.infrastructure.navigation.compose.nav3.graph

import androidx.navigation3.runtime.EntryProviderScope
import com.velord.infrastructure.navigation.compose.nav3.GraphNav3
import com.velord.infrastructure.navigation.compose.nav3.navigator.BottomNavigatorNav3
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationVM
import com.velord.ui.feature.setting.SettingScreen
import com.velord.ui.sharedviewmodel.LanguageVM
import com.velord.ui.sharedviewmodel.ThemeVM
import org.koin.compose.viewmodel.koinViewModel

internal fun EntryProviderScope<GraphNav3>.setupBottomNavigationGraphNav3(
    navigator: BottomNavigatorNav3,
) {
    setupCameraRecordingGraphNav3(navigator)
    setupDemoGraphNav3(navigator)

    entry<GraphNav3.BottomTab.SettingDestinationNav3> {
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
