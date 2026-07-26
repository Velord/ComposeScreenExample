package com.velord.usecase.camera

import com.velord.model.camera.CameraVideoAsset
import kotlinx.coroutines.flow.StateFlow

fun interface GetLastCameraVideoAssetUC : () -> StateFlow<CameraVideoAsset?>
