package com.velord.ui.feature.camerarecording.viewModel

import com.velord.infrastructure.util.permission.PermissionGrantState
import com.velord.model.camera.CameraVideoQuality

sealed interface CameraRecordingUiAction {
    data object CreateCameraSession : CameraRecordingUiAction
    data object SettingsClick : CameraRecordingUiAction
    data object ChangeCameraSelector : CameraRecordingUiAction
    data object CheckPermissionClick : CameraRecordingUiAction
    data object StartStopRecording : CameraRecordingUiAction
    data class ChangeVideoQuality(val quality: CameraVideoQuality) : CameraRecordingUiAction
    data class ChangeIsAudioEnabled(val enabled: Boolean) : CameraRecordingUiAction
    data class UpdateCameraPermissionGrantState(
        val state: PermissionGrantState,
    ) : CameraRecordingUiAction
    data class UpdateAudioPermissionGrantState(
        val state: PermissionGrantState,
    ) : CameraRecordingUiAction
    data class UpdatePermissionGrantState(val state: PermissionGrantState) : CameraRecordingUiAction
}
