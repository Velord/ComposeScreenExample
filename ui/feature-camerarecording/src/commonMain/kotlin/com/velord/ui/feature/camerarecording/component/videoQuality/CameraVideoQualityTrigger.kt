package com.velord.ui.feature.camerarecording.component.videoQuality

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velord.core.resource.Res
import com.velord.core.resource.video_quality
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.model.camera.config.CameraVideoQuality
import com.velord.ui.feature.camerarecording.util.shortLabel
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CameraVideoQualityTrigger(
    quality: CameraVideoQuality,
    isEnabled: Boolean,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                enabled = isEnabled,
                onClick = onClick,
            )
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(quality.shortLabel),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
            ),
        )
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowDown,
            contentDescription = contentDescription,
            modifier = Modifier
                .padding(start = 2.dp)
                .size(18.dp),
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    CameraVideoQualityTrigger(
        quality = CameraVideoQuality.FullHd,
        isEnabled = true,
        contentDescription = stringResource(Res.string.video_quality),
        onClick = {},
    )
}
