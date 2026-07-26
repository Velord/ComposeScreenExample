package com.velord.ui.feature.camerarecording

enum class CameraRecordingNavigationEvent {
    Setting,
}

interface CameraRecordingNavigator {
    fun goToSettingFromCameraRecording()
}
