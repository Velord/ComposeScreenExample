package com.velord.model.camera

enum class CameraRecordingState {
    Idle,
    Recording,
    Paused;

    val isIdle: Boolean get() = this == Idle
}
