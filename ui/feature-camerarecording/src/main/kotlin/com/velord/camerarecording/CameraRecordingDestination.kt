package com.velord.camerarecording

enum class CameraRecordingNavigationEvent {
    SETTINGS
}

interface CameraRecordingNavigator {
    fun goToSettingFromCameraRecording()
}
