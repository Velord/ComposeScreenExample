package com.velord.infrastructure.navigation.compose.destinations.graph

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.parameters.CodeGenVisibility
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationVM
import com.velord.ui.feature.camerarecording.CameraRecordingNavigationEvent
import com.velord.ui.feature.camerarecording.CameraRecordingNavigator
import com.velord.ui.feature.camerarecording.CameraRecordingScreen
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingVM
import org.koin.compose.viewmodel.koinViewModel

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
    val bottomNavVM = koinViewModel<BottomNavigationVM>()
    CameraRecordingScreen(
        viewModel = viewModel,
        needToHandlePermission = true,
        onNavigationEvent = {
            when (it) {
                CameraRecordingNavigationEvent.Setting -> navigator.goToSettingFromCameraRecording()
            }
        },
        onGraphCompleted = {
            bottomNavVM.onAction(BottomNavigationUiAction.GraphCompletedHandling)
        },
        onBackClick = {
            bottomNavVM.onAction(BottomNavigationUiAction.BackRequest)
        }
    )
}
