package com.velord.ui.feature.camerarecording.util

import com.velord.core.resource.Res
import com.velord.core.resource.video_quality_full_hd
import com.velord.core.resource.video_quality_full_hd_short
import com.velord.core.resource.video_quality_hd
import com.velord.core.resource.video_quality_sd
import com.velord.core.resource.video_quality_ultra_hd
import com.velord.core.resource.video_quality_ultra_hd_short
import com.velord.model.camera.config.CameraVideoQuality
import org.jetbrains.compose.resources.StringResource

internal val CameraVideoQuality.shortLabel: StringResource get() = when (this) {
    CameraVideoQuality.Sd -> Res.string.video_quality_sd
    CameraVideoQuality.Hd -> Res.string.video_quality_hd
    CameraVideoQuality.FullHd -> Res.string.video_quality_full_hd_short
    CameraVideoQuality.UltraHd -> Res.string.video_quality_ultra_hd_short
}

internal val CameraVideoQuality.label: StringResource get() = when (this) {
    CameraVideoQuality.Sd -> Res.string.video_quality_sd
    CameraVideoQuality.Hd -> Res.string.video_quality_hd
    CameraVideoQuality.FullHd -> Res.string.video_quality_full_hd
    CameraVideoQuality.UltraHd -> Res.string.video_quality_ultra_hd
}
