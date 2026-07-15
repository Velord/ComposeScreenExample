package com.velord.usecase.camera

import com.velord.model.camera.CameraSessionWrapper
import kotlinx.coroutines.flow.StateFlow

fun interface GetCameraSessionUC : () -> StateFlow<CameraSessionWrapper?>
