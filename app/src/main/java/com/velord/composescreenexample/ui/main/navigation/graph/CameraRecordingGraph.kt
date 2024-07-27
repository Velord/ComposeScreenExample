package com.velord.composescreenexample.ui.main.navigation.graph

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavGraph
import com.velord.camerarecording.CameraRecordingNavigationEvent
import com.velord.camerarecording.CameraRecordingNavigator
import com.velord.camerarecording.CameraRecordingScreen
import com.velord.camerarecording.CameraRecordingViewModel
import org.koin.androidx.compose.koinViewModel

private const val CAMERA_RECORDING_GRAPH = "camera_recording_graph"
@NavGraph<BottomNavigationGraph>(
    route = CAMERA_RECORDING_GRAPH
)
annotation class CameraRecordingGraph

@Destination<CameraRecordingGraph>(start = true)
@Composable
fun CameraRecordingDestination(navigator: CameraRecordingNavigator) {
    val viewModel = koinViewModel<CameraRecordingViewModel>()

    CameraRecordingScreen(viewModel, true) {
        when (it) {
            CameraRecordingNavigationEvent.SETTINGS -> navigator.goToSettingsFromCameraRecording()
        }
    }
}