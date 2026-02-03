package com.velord.navigation.compose.vanilla.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.velord.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import com.velord.camerarecording.CameraRecordingNavigationEvent
import com.velord.camerarecording.CameraRecordingNavigator
import com.velord.camerarecording.CameraRecordingScreen
import com.velord.camerarecording.CameraRecordingViewModel
import com.velord.navigation.compose.vanilla.GraphVanilla
import org.koin.androidx.compose.koinViewModel

internal fun NavGraphBuilder.setupCameraRecordingGraphVanilla(navigator: CameraRecordingNavigator)  {
    navigation<GraphVanilla.BottomTab.CameraRecording.Self>(
        startDestination = GraphVanilla.BottomTab.CameraRecording.CameraRecordingDestinationVanilla
    ) {
        composable<GraphVanilla.BottomTab.CameraRecording.CameraRecordingDestinationVanilla> {
            val viewModel = koinViewModel<CameraRecordingViewModel>()
            val bottomNavViewModel = koinViewModel<BottomNavigationDestinationsVM>()
            CameraRecordingScreen(
                viewModel = viewModel,
                needToHandlePermission = true,
                onNavigationEvent = {
                    when (it) {
                        CameraRecordingNavigationEvent.SETTINGS -> navigator.goToSettingFromCameraRecording()
                    }
                },
                onBackClick = {
                    bottomNavViewModel.graphCompletedHandling()
                }
            )
        }
    }
}