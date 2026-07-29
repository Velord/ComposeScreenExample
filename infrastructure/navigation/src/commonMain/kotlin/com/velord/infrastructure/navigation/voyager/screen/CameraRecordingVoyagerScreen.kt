package com.velord.infrastructure.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.velord.core.navigation.voyager.SharedScreenVoyager
import com.velord.core.navigation.voyager.findRootNavigatorOrCurrent
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationVM
import com.velord.ui.feature.camerarecording.CameraRecordingScreen
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingVM
import org.koin.compose.viewmodel.koinViewModel

internal object CameraRecordingVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<CameraRecordingVM>()
        val bottomNavVM = koinViewModel<BottomNavigationVM>()
        val navigator = LocalNavigator.current.findRootNavigatorOrCurrent()
        val settingScreen = rememberScreen(SharedScreenVoyager.BottomNavigationTab.Settings)

        CameraRecordingScreen(
            viewModel = viewModel,
            needToHandlePermission = true,
            onNavigationEvent = { navigator?.push(settingScreen) },
            onGraphCompleted = {
                bottomNavVM.onAction(BottomNavigationUiAction.GraphCompletedHandling)
            },
            onBackClick = {
                bottomNavVM.onAction(BottomNavigationUiAction.BackRequest)
            }
        )
    }
}
