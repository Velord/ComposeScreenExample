package com.velord.navigation.compose.destinations.graph

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.parameters.CodeGenVisibility
import com.velord.camerarecording.CameraRecordingNavigationEvent
import com.velord.camerarecording.CameraRecordingNavigator
import com.velord.camerarecording.CameraRecordingScreen
import com.velord.camerarecording.CameraRecordingViewModel
import org.koin.androidx.compose.koinViewModel

private const val CAMERA_RECORDING_GRAPH = "camera_recording_graph"
@NavGraph<BottomNavigationGraph>(
    route = CAMERA_RECORDING_GRAPH,
    visibility = CodeGenVisibility.INTERNAL
)
annotation class CameraRecordingGraph

@Destination<CameraRecordingGraph>(start = true)
@Composable
internal fun CameraRecordingDestination(navigator: CameraRecordingNavigator) {
    val viewModel = koinViewModel<CameraRecordingViewModel>()

    CameraRecordingScreen(viewModel, true) {
        when (it) {
            CameraRecordingNavigationEvent.SETTINGS -> navigator.goToSettingFromCameraRecording()
        }
    }
}