package com.velord.ui.feature.camerarecording.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SettingsApplications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velord.core.ui.compose.preview.PreviewCombined

@Composable
internal fun BoxScope.CameraSettingsButton(
    onClick: () -> Unit,
    enabled: Boolean,
) {
    Icon(
        imageVector = Icons.Filled.SettingsApplications,
        contentDescription = null,
        modifier = Modifier
            .align(Alignment.TopEnd)
            .statusBarsPadding()
            .padding(16.dp)
            .size(40.dp)
            .clickable(enabled = enabled) { onClick() },
        tint = MaterialTheme.colorScheme.surfaceVariant,
    )
}

@PreviewCombined
@Composable
private fun Preview() {
    Box {
        CameraSettingsButton(
            onClick = {},
            enabled = true,
        )
    }
}
