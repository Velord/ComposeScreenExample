package com.velord.navigation.compose.nav3.graph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.velord.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import com.velord.bottomnavigation.viewmodel.BottomNavigationVoyagerVM
import com.velord.navigation.compose.nav3.GraphNav3
import com.velord.navigation.compose.nav3.navigator.BottomNavigatorNav3
import com.velord.navigation.voyager.screen.SettingsVoyagerScreen
import com.velord.setting.SettingScreen
import com.velord.sharedviewmodel.ThemeViewModel
import org.koin.androidx.compose.koinViewModel

internal fun EntryProviderScope<NavKey>.setupBottomNavigationNav3(navigator: BottomNavigatorNav3) {
    setupCameraRecordingGraphNav3(navigator)
    setupDemoGraphNav3(navigator)

    entry<GraphNav3.BottomTab.SettingDestinationNav3> {
        val viewModel = koinViewModel<ThemeViewModel>()
        val bottomNavVM = koinViewModel<BottomNavigationDestinationsVM>()
        SettingScreen(
            viewModel = viewModel,
            onBackClick = {
                bottomNavVM.graphCompletedHandling()
            }
        )
    }
}