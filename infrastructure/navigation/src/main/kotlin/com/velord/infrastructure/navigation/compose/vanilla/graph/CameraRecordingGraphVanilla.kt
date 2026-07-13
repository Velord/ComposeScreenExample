package com.velord.infrastructure.navigation.compose.vanilla.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.velord.infrastructure.navigation.compose.vanilla.GraphVanilla
import com.velord.ui.feature.bottomnavigation.viewmodel.destinations.BottomNavigationDestinationsUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.destinations.BottomNavigationDestinationsVM
import com.velord.ui.feature.camerarecording.CameraRecordingNavigationEvent
import com.velord.ui.feature.camerarecording.CameraRecordingNavigator
import com.velord.ui.feature.camerarecording.CameraRecordingScreen
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingVM
import org.koin.compose.viewmodel.koinViewModel

internal fun NavGraphBuilder.setupCameraRecordingGraphVanilla(navigator: CameraRecordingNavigator)  {
    navigation<GraphVanilla.BottomTab.CameraRecording.Self>(
        startDestination = GraphVanilla.BottomTab.CameraRecording.CameraRecordingDestinationVanilla
    ) {
        composable<GraphVanilla.BottomTab.CameraRecording.CameraRecordingDestinationVanilla> {
            val viewModel = koinViewModel<CameraRecordingVM>()
            val bottomNavVM = koinViewModel<BottomNavigationDestinationsVM>()

            CameraRecordingScreen(
                viewModel = viewModel,
                needToHandlePermission = true,
                onNavigationEvent = {
                    when (it) {
                        CameraRecordingNavigationEvent.SETTINGS -> navigator.goToSettingFromCameraRecording()
                    }
                },
                onBackClick = {
                    bottomNavVM.onAction(
                        BottomNavigationDestinationsUiAction.GraphCompletedHandling
                    )
                }
            )
        }
    }
}
