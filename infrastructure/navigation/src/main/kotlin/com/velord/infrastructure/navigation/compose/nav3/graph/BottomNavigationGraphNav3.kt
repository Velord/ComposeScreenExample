package com.velord.infrastructure.navigation.compose.nav3.graph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.velord.infrastructure.navigation.compose.nav3.GraphNav3
import com.velord.infrastructure.navigation.compose.nav3.navigator.BottomNavigatorNav3
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationDestinationsUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import com.velord.ui.feature.setting.SettingScreen
import com.velord.ui.sharedviewmodel.ThemeVM
import org.koin.androidx.compose.koinViewModel

internal fun EntryProviderScope<NavKey>.setupBottomNavigationGraphNav3(navigator: BottomNavigatorNav3) {
    setupCameraRecordingGraphNav3(navigator)
    setupDemoGraphNav3(navigator)

    entry<GraphNav3.BottomTab.SettingDestinationNav3> {
        val viewModel = koinViewModel<ThemeVM>()
        val bottomNavVM = koinViewModel<BottomNavigationDestinationsVM>()
        SettingScreen(
            viewModel = viewModel,
            onBackClick = {
                bottomNavVM.onAction(BottomNavigationDestinationsUiAction.GraphCompletedHandling)
            }
        )
    }
}
