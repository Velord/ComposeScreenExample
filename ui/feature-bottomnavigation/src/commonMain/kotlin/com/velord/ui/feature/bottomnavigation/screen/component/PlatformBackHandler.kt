package com.velord.ui.feature.bottomnavigation.screen.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal expect fun PlatformBackHandler(
    message: String,
    isEnabled: Boolean,
    modifier: Modifier,
    onBackDoubleClick: () -> Unit,
    content: @Composable (String) -> Unit,
)

@Composable
internal expect fun PlatformSingleBackHandler(isEnabled: Boolean, onBackClick: () -> Unit)
