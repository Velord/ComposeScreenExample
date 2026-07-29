package com.velord.ui.feature.camerarecording.component.videoQuality

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.model.camera.config.CameraVideoQuality

@Composable
internal fun CameraVideoQualityMenu(
    expanded: Boolean,
    selectedQuality: CameraVideoQuality,
    onDismissRequest: () -> Unit,
    onQualityClick: (CameraVideoQuality) -> Unit,
) {
    MaterialTheme(
        shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(16.dp)),
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
            ),
        ) {
            CameraVideoQuality.entries.forEach { quality ->
                CameraVideoQualityMenuItem(
                    quality = quality,
                    isSelected = selectedQuality == quality,
                    onClick = { onQualityClick(quality) },
                )
            }
        }
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    CameraVideoQualityMenu(
        expanded = true,
        selectedQuality = CameraVideoQuality.FullHd,
        onDismissRequest = {},
        onQualityClick = {},
    )
}
