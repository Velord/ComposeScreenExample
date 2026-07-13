package com.velord.infrastructure.navigation.compose.vanilla.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.velord.infrastructure.navigation.compose.vanilla.GraphVanilla
import com.velord.infrastructure.navigation.compose.vanilla.navigator.BottomNavigatorVanilla
import com.velord.ui.feature.bottomnavigation.viewmodel.destinations.BottomNavigationDestinationsUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.destinations.BottomNavigationDestinationsVM
import com.velord.ui.feature.setting.SettingScreen
import com.velord.ui.sharedviewmodel.ThemeVM
import org.koin.compose.viewmodel.koinViewModel

internal fun NavGraphBuilder.setupBottomNavigationGraphVanilla(navigator: BottomNavigatorVanilla) {
    setupCameraRecordingGraphVanilla(navigator)
    setupDemoGraphVanilla(navigator)

    composable<GraphVanilla.BottomTab.SettingDestinationVanilla> {
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
