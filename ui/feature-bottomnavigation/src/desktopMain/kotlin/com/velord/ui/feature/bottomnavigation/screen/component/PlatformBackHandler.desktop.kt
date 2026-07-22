package com.velord.ui.feature.bottomnavigation.screen.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun PlatformBackHandler(
    message: String,
    isEnabled: Boolean,
    modifier: Modifier,
    onBackDoubleClick: () -> Unit,
    content: @Composable (String) -> Unit,
) {
    // Desktop has no system back button; closing the window remains a platform-owned action.
}

@Composable
internal actual fun PlatformSingleBackHandler(isEnabled: Boolean, onBackClick: () -> Unit) {
    // Desktop has no system back button; closing the window remains a platform-owned action.
}
