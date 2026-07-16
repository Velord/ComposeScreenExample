package com.velord.model.camera

import com.velord.model.camera.config.CameraLens

data class CameraState(
    val isReady: Boolean,
    val lens: CameraLens,
    val recordingState: CameraRecordingState,
    val recordingDurationMillis: Long,
    val errorMessage: String?,
) {
    companion object {
        val DEFAULT = CameraState(
            isReady = false,
            lens = CameraLens.Front,
            recordingState = CameraRecordingState.Idle,
            recordingDurationMillis = 0L,
            errorMessage = null,
        )
    }
}
