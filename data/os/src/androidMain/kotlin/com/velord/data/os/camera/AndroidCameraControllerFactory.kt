package com.velord.data.os.camera

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope
import com.kashif.cameraK.builder.createAndroidCameraControllerBuilder as kameraAndroidBuilder
import com.kashif.cameraK.controller.CameraController as KameraController
import com.kashif.cameraK.state.CameraConfiguration as KameraConfiguration

class AndroidCameraControllerFactory(
    private val context: Context,
) : CameraControllerFactory {

    override suspend fun create(
        config: KameraConfiguration
    ): KameraController = withContext(Dispatchers.Main) {
        kameraAndroidBuilder(context, ProcessLifecycleOwner.get()).apply {
            setFlashMode(config.flashMode)
            setTorchMode(config.torchMode)
            setCameraLens(config.cameraLens)
            setImageFormat(config.imageFormat)
            setQualityPrioritization(config.qualityPrioritization)
            setPreferredCameraDeviceType(config.cameraDeviceType)
            setAspectRatio(config.aspectRatio)
            setDirectory(config.directory)
            setMirrorFrontCamera(config.mirrorFrontCamera)
            config.targetResolution?.let { resolution ->
                setResolution(resolution.first, resolution.second)
            }
        }
            .build()
    }
}

@Module
actual class CameraPlatformModule {
    @Single
    actual fun provideCameraControllerFactory(scope: Scope): CameraControllerFactory =
        AndroidCameraControllerFactory(scope.get())
}
