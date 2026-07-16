package com.velord.model.camera.config

data class CameraRecordingConfig(
    val lens: CameraLens,
    val quality: CameraVideoQuality,
    val audioConfig: CameraAudioConfig,
)
