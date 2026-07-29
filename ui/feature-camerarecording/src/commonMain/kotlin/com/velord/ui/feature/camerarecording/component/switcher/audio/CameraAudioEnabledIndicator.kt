package com.velord.ui.feature.camerarecording.component.switcher.audio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.velord.core.ui.compose.preview.PreviewCombined

@Composable
internal fun CameraAudioEnabledIndicator() {
    Box(
        modifier = Modifier
            .padding(start = 8.dp)
            .size(8.dp)
            .clip(CircleShape)
            .background(Color(0xFFA87BF7)),
    )
}

@PreviewCombined
@Composable
private fun Preview() {
    CameraAudioEnabledIndicator()
}
