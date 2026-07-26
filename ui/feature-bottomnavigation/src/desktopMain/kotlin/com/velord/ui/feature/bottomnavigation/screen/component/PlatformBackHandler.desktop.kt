package com.velord.ui.feature.bottomnavigation.screen.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationBackBehavior

@Composable
internal actual fun PlatformBackHandler(
    backBehavior: BottomNavigationBackBehavior,
    message: String,
    modifier: Modifier,
    onBackClick: () -> Unit,
    onBackDoubleClick: () -> Unit,
    content: @Composable (String) -> Unit,
) {
    // Desktop has no system back button; closing the window remains a platform-owned action.
}
