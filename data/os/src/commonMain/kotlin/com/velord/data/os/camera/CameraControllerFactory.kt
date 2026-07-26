package com.velord.data.os.camera

import com.kashif.cameraK.controller.CameraController as KameraController
import com.kashif.cameraK.state.CameraConfiguration as KameraConfiguration

interface CameraControllerFactory {
    suspend fun create(config: KameraConfiguration): KameraController
}