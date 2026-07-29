package com.velord.ui.feature.camerarecording.viewModel

import com.velord.infrastructure.util.permission.PermissionGrantState
import com.velord.model.camera.CameraRecordingState
import com.velord.model.camera.CameraSessionWrapper
import com.velord.model.camera.CameraState
import com.velord.model.camera.CameraVideoAsset
import com.velord.model.camera.config.CameraVideoQuality

data class PermissionUiState(
    val camera: PermissionGrantState,
    val audio: PermissionGrantState,
)

data class CameraRecordingUiState(
    val permissionState: PermissionUiState,
    val videoQuality: CameraVideoQuality,
    val isAudioEnabled: Boolean,
    val isCameraLensSwitchAvailable: Boolean,
    val cameraState: CameraState,
    val cameraSession: CameraSessionWrapper?,
    val lastVideoAsset: CameraVideoAsset?,
) {
    val isRecordingStarted: Boolean
        get() = cameraState.recordingState != CameraRecordingState.Idle

    companion object {
        val DEFAULT = CameraRecordingUiState(
            permissionState = defaultPermissionUiState(),
            videoQuality = CameraVideoQuality.FullHd,
            isAudioEnabled = true,
            isCameraLensSwitchAvailable = defaultIsCameraLensSwitchAvailable,
            cameraState = CameraState.DEFAULT,
            cameraSession = null,
            lastVideoAsset = null,
        )
    }
}

internal expect fun defaultPermissionUiState(): PermissionUiState

internal expect val defaultIsCameraLensSwitchAvailable: Boolean
