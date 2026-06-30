package com.velord.os.camera

import androidx.camera.video.Recording
import com.velord.model.camera.RecordingSession

internal class CameraXRecordingSession(private val recording: Recording) : RecordingSession {

    override fun stop() {
        recording.stop()
    }
}