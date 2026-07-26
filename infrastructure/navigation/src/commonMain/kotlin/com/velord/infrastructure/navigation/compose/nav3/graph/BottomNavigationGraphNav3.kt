package com.velord.infrastructure.navigation.compose.nav3.graph

import androidx.navigation3.runtime.EntryProviderScope
import com.velord.infrastructure.navigation.compose.nav3.GraphNav3
import com.velord.infrastructure.navigation.compose.nav3.navigator.BottomNavigatorNav3
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationVM
import com.velord.ui.feature.setting.SettingScreen
import com.velord.ui.sharedviewmodel.ThemeVM
import org.koin.compose.viewmodel.koinViewModel

internal fun EntryProviderScope<GraphNav3>.setupBottomNavigationGraphNav3(
    navigator: BottomNavigatorNav3,
) {
    setupCameraRecordingGraphNav3(navigator)
    setupDemoGraphNav3(navigator)

    entry<GraphNav3.BottomTab.SettingDestinationNav3> {
        val viewModel = koinViewModel<ThemeVM>()
        val bottomNavVM = koinViewModel<BottomNavigationVM>()
        SettingScreen(
            viewModel = viewModel,
            onBackClick = {
                bottomNavVM.onAction(BottomNavigationUiAction.GraphCompletedHandling)
            }
        )
    }
}
