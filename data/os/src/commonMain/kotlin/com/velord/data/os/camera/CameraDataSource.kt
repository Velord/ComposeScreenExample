package com.velord.data.os.camera

import com.velord.model.camera.RecordingSession
import com.velord.model.camera.VideoCaptureWrapper
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

interface CameraDataSource {
    fun startRecording(
        videoCapture: VideoCaptureWrapper,
        audioEnabled: Boolean,
    ) : RecordingSession
}

@Module
expect class CameraPlatformModule() {

    @Single
    fun provideCameraDataSource(scope: Scope): CameraDataSource
}
