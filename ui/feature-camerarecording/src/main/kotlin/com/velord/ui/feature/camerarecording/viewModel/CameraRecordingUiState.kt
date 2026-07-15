package com.velord.ui.feature.camerarecording.viewModel

import androidx.camera.core.CameraSelector
import androidx.camera.video.Quality
import com.velord.infrastructure.util.permission.PermissionGrantState
import com.velord.model.camera.RecordingSession

data class PermissionUiState(
    val camera: PermissionGrantState,
    val audio: PermissionGrantState
) {
    companion object {
        val DEFAULT = PermissionUiState(
            camera = PermissionGrantState.NotAsked,
            audio = PermissionGrantState.NotAsked
        )
    }
}

data class CameraRecordingUiState(
    // Permission
    val permissionState: PermissionUiState,
    val videoQuality: Quality,
    val cameraSelector: CameraSelector,
    val isAudioEnabled: Boolean,
    val isRecordingStarted: Boolean,
    // A RecordingSession controls the current active recording without exposing CameraX Recording.
    val recording: RecordingSession?,
) {
    companion object {
        val DEFAULT = CameraRecordingUiState(
            permissionState = PermissionUiState.DEFAULT,
            videoQuality = Quality.HIGHEST,
            cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA,
            isAudioEnabled = true,
            isRecordingStarted = false,
            recording = null,
        )
    }
}