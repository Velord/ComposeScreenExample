package com.velord.ui.feature.camerarecording.util

import com.velord.core.resource.AppString
import com.velord.core.resource.AppStringResource
import com.velord.model.camera.config.CameraVideoQuality

internal val CameraVideoQuality.shortLabel: AppStringResource get() = when (this) {
    CameraVideoQuality.Sd -> AppString.video_quality_sd
    CameraVideoQuality.Hd -> AppString.video_quality_hd
    CameraVideoQuality.FullHd -> AppString.video_quality_full_hd_short
    CameraVideoQuality.UltraHd -> AppString.video_quality_ultra_hd_short
}

internal val CameraVideoQuality.label: AppStringResource get() = when (this) {
    CameraVideoQuality.Sd -> AppString.video_quality_sd
    CameraVideoQuality.Hd -> AppString.video_quality_hd
    CameraVideoQuality.FullHd -> AppString.video_quality_full_hd
    CameraVideoQuality.UltraHd -> AppString.video_quality_ultra_hd
}
