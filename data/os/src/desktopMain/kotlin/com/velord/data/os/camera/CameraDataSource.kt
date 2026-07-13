package com.velord.data.os.camera

import com.velord.model.camera.RecordingSession
import com.velord.model.camera.VideoCaptureWrapper
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
actual class CameraPlatformModule {
    @Single
    actual fun provideCameraDataSource(scope: Scope): CameraDataSource = DesktopCameraDataSource()
}

private class DesktopCameraDataSource : CameraDataSource {

    override fun startRecording(
        videoCapture: VideoCaptureWrapper,
        audioEnabled: Boolean,
    ): RecordingSession {
        TODO("Not yet implemented")
    }
}
