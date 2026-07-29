package com.velord.ui.feature.camerarecording.component.switcher.audio

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velord.core.resource.Res
import com.velord.core.resource.record_audio_on
import com.velord.core.ui.compose.preview.PreviewCombined
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CameraAudioLabel(label: String) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
        ),
        modifier = Modifier.padding(top = 4.dp),
    )
}

@PreviewCombined
@Composable
private fun Preview() {
    CameraAudioLabel(label = stringResource(Res.string.record_audio_on))
}
