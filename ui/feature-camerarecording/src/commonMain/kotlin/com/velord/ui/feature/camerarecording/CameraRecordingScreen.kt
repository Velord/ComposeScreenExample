package com.velord.ui.feature.camerarecording

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.core.ui.util.ObserveSharedFlow
import com.velord.ui.feature.camerarecording.component.Content
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiAction
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiState
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingVM

private val log = Logger.withTag("CameraRecordingScreen")

@Composable
fun CameraRecordingScreen(
    viewModel: CameraRecordingVM,
    needToHandlePermission: Boolean = false,
    onNavigationEvent: (CameraRecordingNavigationEvent) -> Unit,
    onBackClick: () -> Unit,
) {
    val uiState = viewModel.uiStateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.onAction(CameraRecordingUiAction.CreateCameraSession)
    }

    SideEffect {
        // Simulate we completed back stack handling
        onBackClick()
    }

    ObserveSharedFlow(flow = viewModel.navigationEvent) {
        onNavigationEvent(it)
    }

    if (needToHandlePermission) {
        // To annoying. Return back later.
//        CheckCameraAndAudioRecordPermission(
//            triggerCheckEvent = viewModel.checkPermissionEvent,
//            onCameraUpdateState = {
//                val action = CameraRecordingUiAction.UpdateCameraPermissionGrantState(it)
//                viewModel.onAction(action)
//            },
//            onMicroUpdateState = {
//                val action = CameraRecordingUiAction.UpdateAudioPermissionGrantState(it)
//                viewModel.onAction(action)
//            }
//        )
    }

    log.d { "permissionCameraState: ${uiState.value.permissionState.camera}" }
    log.d { "permissionAudioState: ${uiState.value.permissionState.audio}" }
    Content(
        uiState = uiState.value,
        onAction = viewModel::onAction,
    )
}

@PreviewCombined
@Composable
private fun Preview() {
    Content(
        uiState = CameraRecordingUiState.DEFAULT,
        onAction = {},
    )
}
