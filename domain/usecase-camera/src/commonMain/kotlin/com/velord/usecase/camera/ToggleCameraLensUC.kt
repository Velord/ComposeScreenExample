package com.velord.usecase.camera

import com.velord.model.camera.CameraRecordingConfig

fun interface ToggleCameraLensUC : suspend (CameraRecordingConfig) -> Boolean
