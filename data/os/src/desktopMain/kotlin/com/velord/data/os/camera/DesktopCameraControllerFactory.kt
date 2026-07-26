package com.velord.data.os.camera

import com.kashif.cameraK.controller.CameraController as KameraController
import com.kashif.cameraK.controller.DesktopCameraControllerBuilder as KameraDesktopBuilder
import com.kashif.cameraK.state.CameraConfiguration as KameraConfiguration

class DesktopCameraControllerFactory : CameraControllerFactory {

    override suspend fun create(
        config: KameraConfiguration
    ): KameraController = KameraDesktopBuilder().apply {
        setImageFormat(config.imageFormat)
        setDirectory(config.directory)
        setAspectRatio(config.aspectRatio)
        setMirrorFrontCamera(config.mirrorFrontCamera)
        config.targetResolution?.let { resolution ->
            setResolution(resolution.first, resolution.second)
        }
    }.build()
}
