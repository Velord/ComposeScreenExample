package com.velord.infrastructure.navigation.compose.destinations.graph

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.parameters.CodeGenVisibility
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationDestinationsUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import com.velord.ui.feature.camerarecording.CameraRecordingNavigationEvent
import com.velord.ui.feature.camerarecording.CameraRecordingNavigator
import com.velord.ui.feature.camerarecording.CameraRecordingScreen
import com.velord.ui.feature.camerarecording.CameraRecordingVM
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
            bottomNavVM.onAction(BottomNavigationDestinationsUiAction.GraphCompletedHandling)
        }
    )
}
