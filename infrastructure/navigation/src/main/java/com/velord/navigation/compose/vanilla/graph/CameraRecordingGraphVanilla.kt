package com.velord.navigation.compose.vanilla.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.velord.camerarecording.CameraRecordingNavigationEvent
import com.velord.camerarecording.CameraRecordingNavigator
import com.velord.camerarecording.CameraRecordingScreen
import com.velord.camerarecording.CameraRecordingViewModel
import com.velord.navigation.compose.vanilla.GraphVanilla
import org.koin.androidx.compose.koinViewModel

internal fun NavGraphBuilder.setupCameraRecordingGraph(navigator: CameraRecordingNavigator)  {
    navigation<GraphVanilla.BottomTab.CameraRecording.Self>(
        startDestination = GraphVanilla.BottomTab.CameraRecording.CameraRecordingDestinationVanilla
    ) {
        composable<GraphVanilla.BottomTab.CameraRecording.CameraRecordingDestinationVanilla> {
            val viewModel = koinViewModel<CameraRecordingViewModel>()
            CameraRecordingScreen(viewModel, true) {
                when (it) {
                    CameraRecordingNavigationEvent.SETTINGS -> navigator.goToSettingFromCameraRecording()
                }
            }
        }
    }
}