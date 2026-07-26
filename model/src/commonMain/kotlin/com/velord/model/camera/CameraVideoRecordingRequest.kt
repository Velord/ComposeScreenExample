package com.velord.model.camera

import com.velord.model.camera.config.CameraRecordingConfig
import com.velord.model.file.FileName

data class CameraVideoRecordingRequest(
    val session: CameraSessionWrapper,
    val config: CameraRecordingConfig,
    val outputDirectory: String,
    val filePrefix: FileName,
)
