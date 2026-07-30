package com.velord.ui.feature.bottomnavigation.screen.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import com.velord.core.ui.compose.component.LocalDesktopBackDispatcher
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
    val dispatcher = LocalDesktopBackDispatcher.current
    val currentOnBackClick by rememberUpdatedState(onBackClick)

    DisposableEffect(dispatcher, backBehavior) {
        val handler: () -> Boolean = {
            when (backBehavior) {
                BottomNavigationBackBehavior.DelegateToNavigator -> {
                    currentOnBackClick()
                    false
                }
                else -> {
                    currentOnBackClick()
                    true
                }
            }
        }
        dispatcher?.register(handler)
        onDispose { dispatcher?.unregister(handler) }
    }
}
