package com.velord.usecase.camera

import com.velord.model.camera.config.CameraRecordingConfig

fun interface StartRecordingUC : (CameraRecordingConfig) -> Unit
