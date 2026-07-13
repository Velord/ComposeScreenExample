package com.velord.ui.feature.camerarecording.viewModel

import androidx.camera.video.Quality
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import com.velord.infrastructure.util.permission.PermissionGrantState

sealed interface CameraRecordingUiAction {
    data object SettingsClick : CameraRecordingUiAction
    data object ChangeCameraSelector : CameraRecordingUiAction
    data object CheckPermissionClick : CameraRecordingUiAction
    data class ChangeVideoQuality(val quality: Quality) : CameraRecordingUiAction
    data class ChangeIsAudioEnabled(val enabled: Boolean) : CameraRecordingUiAction
    data class StartStopRecording(val newCapture: VideoCapture<Recorder>?) : CameraRecordingUiAction
    data class UpdateCameraPermissionGrantState(
        val state: PermissionGrantState,
    ) : CameraRecordingUiAction
    data class UpdateAudioPermissionGrantState(
        val state: PermissionGrantState,
    ) : CameraRecordingUiAction
    data class UpdatePermissionGrantState(val state: PermissionGrantState) : CameraRecordingUiAction
}