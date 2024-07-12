package com.velord.composescreenexample.ui.main.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavGraph
import com.velord.camerarecording.CameraRecordingNavigator
import com.velord.camerarecording.CameraRecordingScreen
import com.velord.camerarecording.CameraRecordingViewModel
import kotlinx.coroutines.flow.filterNotNull
import org.koin.androidx.compose.koinViewModel

private const val CAMERA_RECORDING_GRAPH = "camera_recording_graph"
@NavGraph<BottomNavigationGraph>(
    route = CAMERA_RECORDING_GRAPH,
    start = true
)
annotation class CameraRecordingGraph

@Destination<CameraRecordingGraph>(start = true)
@Composable
fun CameraRecordingDestination(navigator: CameraRecordingNavigator) {
    val viewModel = koinViewModel<CameraRecordingViewModel>()

    val navigationState = viewModel.navigationEventDestination
        .collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(key1 = navigationState) {
        snapshotFlow { navigationState.value }
            .filterNotNull()
            .collect {
                navigator.goToSettingsFromCameraRecording()
            }
    }

    CameraRecordingScreen(viewModel, true)
}