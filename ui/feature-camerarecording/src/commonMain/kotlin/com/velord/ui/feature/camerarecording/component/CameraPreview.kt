package com.velord.ui.feature.camerarecording.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiState
import com.kashif.cameraK.compose.CameraPreviewView as KameraPreviewView
import com.kashif.cameraK.state.CameraKState as KameraState

@Composable
internal fun CameraPreview(uiState: CameraRecordingUiState) {
    val permissionCamera = uiState.permissionState.camera
    val permissionAudio = uiState.permissionState.audio
    val hasRequiredPermission = permissionCamera.isGranted &&
        (uiState.isAudioEnabled.not() || permissionAudio.isGranted)
    if (hasRequiredPermission.not()) return

    val session = uiState.cameraSession ?: return
    val cameraState = session.value.cameraState.collectAsStateWithLifecycle()
    val readyState = cameraState.value as? KameraState.Ready ?: return
    KameraPreviewView(
        controller = readyState.controller,
        modifier = Modifier.fillMaxSize(),
    )
}

@PreviewCombined
@Composable
private fun Preview() {
    CameraPreview(uiState = CameraRecordingUiState.DEFAULT)
}
