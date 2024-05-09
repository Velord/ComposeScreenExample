package com.velord.camerarecording

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import com.velord.uicore.utils.permission.CheckCameraAndAudioRecordPermission
import com.velord.uicore.utils.permission.toPermissionState
import kotlinx.coroutines.flow.filterNotNull
import org.koin.androidx.compose.koinViewModel

interface CameraRecordingNavigator {
    fun goToSettingsFromCameraRecording()
}

@OptIn(ExperimentalPermissionsApi::class)
@Destination<ExternalModuleGraph>
@Composable
fun CameraRecordingDestination(
    navigator: CameraRecordingNavigator
) {
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

    CheckCameraAndAudioRecordPermission(
        triggerCheckEvent = viewModel.checkPermissionEvent,
        onCameraUpdateState = {
            val state = it.status.toPermissionState()
            viewModel.updateCameraPermissionState(state)
        },
        onMicroUpdateState = {
            val state = it.status.toPermissionState()
            viewModel.updateAudioPermissionState(state)
        }
    )

    CameraRecordingScreen(viewModel)
}