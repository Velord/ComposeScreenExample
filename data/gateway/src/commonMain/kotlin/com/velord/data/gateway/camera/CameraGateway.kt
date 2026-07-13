package com.velord.data.gateway.camera

import com.velord.data.os.camera.CameraDataSource
import com.velord.model.camera.RecordingSession
import com.velord.model.camera.VideoCaptureWrapper
import org.koin.core.annotation.Single

@Single
class CameraGateway(private val dataSource: CameraDataSource) {
    fun startRecording(
        videoCapture: VideoCaptureWrapper,
        audioEnabled: Boolean,
    ): RecordingSession = dataSource.startRecording(
        videoCapture = videoCapture,
        audioEnabled = audioEnabled,
    )
}
