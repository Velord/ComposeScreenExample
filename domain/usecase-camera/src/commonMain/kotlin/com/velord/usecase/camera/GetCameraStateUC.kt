package com.velord.usecase.camera

import com.velord.model.camera.CameraState
import kotlinx.coroutines.flow.StateFlow

fun interface GetCameraStateUC : () -> StateFlow<CameraState>
