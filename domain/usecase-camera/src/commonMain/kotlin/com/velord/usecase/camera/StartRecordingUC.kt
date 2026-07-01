package com.velord.usecase.camera

import com.velord.model.camera.RecordingSession
import com.velord.model.camera.VideoCaptureWrapper

fun interface StartRecordingUC {
    operator fun invoke(
        videoCapture: VideoCaptureWrapper,
        audioEnabled: Boolean,
    ): RecordingSession
}