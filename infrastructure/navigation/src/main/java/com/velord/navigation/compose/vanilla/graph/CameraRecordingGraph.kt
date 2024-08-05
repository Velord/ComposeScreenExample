package com.velord.navigation.compose.vanilla.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.velord.camerarecording.CameraRecordingNavigationEvent
import com.velord.camerarecording.CameraRecordingNavigator
import com.velord.camerarecording.CameraRecordingScreen
import com.velord.camerarecording.CameraRecordingViewModel
import com.velord.navigation.compose.vanilla.CameraRecordingDestinationVanilla
import com.velord.navigation.compose.vanilla.CameraRecordingGraphVanilla
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.setupCameraRecordingGraph(navigator: CameraRecordingNavigator)  {
    navigation<CameraRecordingGraphVanilla>(startDestination = CameraRecordingDestinationVanilla) {
        composable<CameraRecordingDestinationVanilla> {
            val viewModel = koinViewModel<CameraRecordingViewModel>()

            CameraRecordingScreen(viewModel, true) {
                when (it) {
                    CameraRecordingNavigationEvent.SETTINGS -> navigator.goToSettingsFromCameraRecording()
                }
            }
        }
    }
}