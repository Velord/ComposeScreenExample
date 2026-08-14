package com.velord.ui.feature.camerarecording.component.switcher.lens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.velord.core.resource.AppString
import com.velord.core.resource.stringResource
import com.velord.core.ui.compose.preview.PreviewCombined

@Composable
internal fun CameraLensLabel(
    text: String,
    isSelected: Boolean,
    accentColor: Color,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Medium,
            color = lensLabelColor(isSelected = isSelected, accentColor = accentColor),
            shadow = lensLabelShadow(isSelected = isSelected, accentColor = accentColor),
        ),
        maxLines = 1,
        softWrap = false,
    )
}

@Composable
private fun lensLabelColor(
    isSelected: Boolean,
    accentColor: Color,
): Color = if (isSelected) {
    accentColor
} else {
    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.78f)
}

private fun lensLabelShadow(
    isSelected: Boolean,
    accentColor: Color,
): Shadow = Shadow(
    color = if (isSelected) accentColor.copy(alpha = 0.82f) else Color.Transparent,
    blurRadius = 8f,
)

@PreviewCombined
@Composable
private fun Preview() {
    CameraLensLabel(
        text = stringResource(AppString.front),
        isSelected = true,
        accentColor = Color(0xFFA87BF7),
    )
}
