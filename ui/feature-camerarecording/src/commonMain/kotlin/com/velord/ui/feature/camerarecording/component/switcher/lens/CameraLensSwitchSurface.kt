package com.velord.ui.feature.camerarecording.component.switcher.lens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.velord.core.resource.Res
import com.velord.core.resource.switch_camera_lens
import com.velord.core.ui.compose.icon.DoubleArrowRightIcon
import com.velord.core.ui.compose.preview.PreviewCombined
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CameraLensSwitchSurface(
    isFront: Boolean,
    accentColor: Color,
    isEnabled: Boolean,
    onClick: () -> Unit,
) {
    val selectorShape = RoundedCornerShape(20.dp)

    Surface(
        modifier = Modifier
            .clip(selectorShape)
            .clickable(
                enabled = isEnabled,
                onClick = onClick,
            )
            .graphicsLayer {
                alpha = lensSwitchAlpha(isEnabled = isEnabled)
            },
        shape = selectorShape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    ) {
        Box(
            modifier = Modifier
                .width(62.dp)
                .height(38.dp)
                .padding(3.dp),
            contentAlignment = if (isFront) Alignment.CenterEnd else Alignment.CenterStart,
        ) {
            CameraLensSwitchGlow(
                isFront = isFront,
                accentColor = accentColor,
            )
            CameraLensSwitchThumb(
                isFront = isFront,
                accentColor = accentColor,
            )
        }
    }
}

@Composable
private fun CameraLensSwitchGlow(
    isFront: Boolean,
    accentColor: Color,
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(lensSwitchGlowColor(isFront = isFront, accentColor = accentColor)),
    )
}

@Composable
private fun CameraLensSwitchThumb(
    isFront: Boolean,
    accentColor: Color,
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(lensSwitchButtonColor(isFront = isFront, accentColor = accentColor)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = DoubleArrowRightIcon,
            contentDescription = stringResource(Res.string.switch_camera_lens),
            modifier = Modifier.size(18.dp),
            tint = lensSwitchIconTint(isFront = isFront),
        )
    }
}

private fun lensSwitchGlowColor(
    isFront: Boolean,
    accentColor: Color,
): Color = if (isFront) {
    accentColor.copy(alpha = 0.4f)
} else {
    Color.White.copy(alpha = 0.3f)
}

private fun lensSwitchButtonColor(
    isFront: Boolean,
    accentColor: Color,
): Color = if (isFront) accentColor else Color.White

private fun lensSwitchAlpha(isEnabled: Boolean) = if (isEnabled) {
    1f
} else {
    0.55f
}

private fun lensSwitchIconTint(isFront: Boolean): Color = if (isFront) {
    Color.White
} else {
    Color(0xFF1E1D24)
}

@PreviewCombined
@Composable
private fun Preview() {
    CameraLensSwitchSurface(
        isFront = true,
        accentColor = Color(0xFFA87BF7),
        isEnabled = true,
        onClick = {},
    )
}
