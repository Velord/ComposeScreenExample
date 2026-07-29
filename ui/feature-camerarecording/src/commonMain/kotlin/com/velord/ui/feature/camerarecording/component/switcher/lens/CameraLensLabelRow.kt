package com.velord.ui.feature.camerarecording.component.switcher.lens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velord.core.resource.Res
import com.velord.core.resource.front
import com.velord.core.resource.rear
import com.velord.core.ui.compose.preview.PreviewCombined
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CameraLensLabelRow(
    isFront: Boolean,
    accentColor: Color,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 4.dp),
    ) {
        CameraLensLabel(
            text = stringResource(Res.string.front),
            isSelected = isFront,
            accentColor = accentColor,
        )
        Text(
            text = "/",
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        )
        CameraLensLabel(
            text = stringResource(Res.string.rear),
            isSelected = isFront.not(),
            accentColor = accentColor,
        )
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    CameraLensLabelRow(
        isFront = true,
        accentColor = Color(0xFFA87BF7),
    )
}
