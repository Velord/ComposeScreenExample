package com.velord.ui.feature.camerarecording.component.videoQuality

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.velord.core.resource.Res
import com.velord.core.resource.selected
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.model.camera.config.CameraVideoQuality
import com.velord.ui.feature.camerarecording.util.label
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CameraVideoQualityMenuItem(
    quality: CameraVideoQuality,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(quality.label),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = videoQualityItemTextColor(isSelected = isSelected),
                        fontWeight = videoQualityItemFontWeight(isSelected = isSelected),
                    ),
                    modifier = Modifier.weight(1f),
                )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = stringResource(Res.string.selected),
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        },
        onClick = onClick,
    )
}

@Composable
private fun videoQualityItemTextColor(isSelected: Boolean) = if (isSelected) {
    MaterialTheme.colorScheme.secondary
} else {
    MaterialTheme.colorScheme.onSurface
}

private fun videoQualityItemFontWeight(isSelected: Boolean) = if (isSelected) {
    FontWeight.Bold
} else {
    FontWeight.Normal
}

@PreviewCombined
@Composable
private fun Preview() {
    CameraVideoQualityMenuItem(
        quality = CameraVideoQuality.FullHd,
        isSelected = true,
        onClick = {},
    )
}
