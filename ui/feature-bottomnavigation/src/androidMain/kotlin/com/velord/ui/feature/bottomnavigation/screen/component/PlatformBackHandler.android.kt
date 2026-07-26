package com.velord.ui.feature.bottomnavigation.screen.component

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.velord.multiplebackstackapplier.utils.compose.SnackBarOnBackPressHandler
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
    when (backBehavior) {
        BottomNavigationBackBehavior.DelegateToNavigator -> Unit
        BottomNavigationBackBehavior.ReturnToDefaultTab -> BackHandler(onBack = onBackClick)
        BottomNavigationBackBehavior.ConfirmExit -> {
            SnackBarOnBackPressHandler(
                message = message,
                modifier = modifier,
                enabled = true,
                onBackClickLessThanDuration = onBackDoubleClick,
            ) { snackbarData ->
                content(snackbarData.visuals.message)
            }
        }
    }
}
