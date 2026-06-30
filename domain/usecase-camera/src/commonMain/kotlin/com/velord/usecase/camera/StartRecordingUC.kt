package com.velord.usecase.camera

import com.velord.model.camera.RecordingSession
import com.velord.model.camera.VideoCaptureRequest

fun interface StartRecordingUC {
    operator fun invoke(
        videoCapture: VideoCaptureRequest,
        audioEnabled: Boolean,
    ): RecordingSession
}