package com.velord.usecase.camera

import com.velord.model.camera.CameraRecordingConfig

fun interface StartRecordingUC : (CameraRecordingConfig) -> Unit
