package com.velord.ui.feature.bottomnavigation.screen.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.velord.multiplebackstackapplier.utils.compose.SnackBarOnBackPressHandler

@Composable
internal actual fun PlatformBackHandler(
    message: String,
    isEnabled: Boolean,
    modifier: Modifier,
    onBackDoubleClick: () -> Unit,
    content: @Composable (String) -> Unit,
) {
    SnackBarOnBackPressHandler(
        message = message,
        modifier = modifier,
        enabled = isEnabled,
        onBackClickLessThanDuration = onBackDoubleClick,
    ) { snackbarData ->
        content(snackbarData.visuals.message)
    }
}
