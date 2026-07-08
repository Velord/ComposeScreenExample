package com.velord.ui.feature.camerarecording

enum class CameraRecordingNavigationEvent {
    SETTINGS
}

interface CameraRecordingNavigator {
    fun goToSettingFromCameraRecording()
}
