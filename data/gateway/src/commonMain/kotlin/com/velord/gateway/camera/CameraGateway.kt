package com.velord.gateway.camera

import com.velord.model.camera.RecordingSession
import com.velord.model.camera.VideoCaptureRequest
import com.velord.os.camera.CameraDataSource
import org.koin.core.annotation.Single

@Single
class CameraGateway(private val dataSource: CameraDataSource) {

    fun startRecording(
        videoCapture: VideoCaptureRequest,
        audioEnabled: Boolean,
    ): RecordingSession = dataSource.startRecording(
        videoCapture = videoCapture,
        audioEnabled = audioEnabled,
    )
}