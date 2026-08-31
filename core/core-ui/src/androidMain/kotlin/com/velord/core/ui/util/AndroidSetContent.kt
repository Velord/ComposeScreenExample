package com.velord.core.ui.util

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.velord.core.ui.compose.component.ToastHost
import com.velord.core.ui.theme.AppThemeHost
import com.velord.core.ui.theme.LocalizationHost
import com.velord.model.ToastConfig
import kotlinx.coroutines.flow.Flow

fun ComponentActivity.setContentWithTheme(
    screen: @Composable ComposeView.() -> Unit,
): ComposeView = ComposeView(this).setContentWithTheme(screen)

fun Fragment.setContentWithTheme(
    screen: @Composable ComposeView.() -> Unit,
): ComposeView = ComposeView(requireContext()).setContentWithTheme(screen)

context(_: LifecycleOwner)
fun ComposeView.setContentWithTheme(
    screen: @Composable ComposeView.() -> Unit,
): ComposeView = setThemedContent {
    screen()
}

context(_: LifecycleOwner)
fun ComposeView.setToastOverlayWithTheme(
    toastEventFlow: Flow<ToastConfig>,
): ComposeView = setThemedContent {
    ToastHost(toastEventFlow = toastEventFlow) {
        // Empty content. This ComposeView is only the global toast overlay.
    }
}

context(_: LifecycleOwner)
private fun ComposeView.setThemedContent(
    content: @Composable ComposeView.() -> Unit,
): ComposeView = apply {
    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

    setContent {
        LocalizationHost {
            AppThemeHost {
                this@setThemedContent.content()
            }
        }
    }
}
