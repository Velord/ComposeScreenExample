package com.velord.ui.feature.bottomnavigation.screen.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.velord.core.resource.Res
import com.velord.core.resource.press_again_to_exit
import org.jetbrains.compose.resources.stringResource

private val log = Logger.withTag("LogBackStack")

@Composable
internal  fun <T> ScreenSetup(
    state: State<T>,
    isBackHandlingEnabled: Boolean,
    onBackDoubleClick: () -> Unit,
    libSetup: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    // Log whenever the state changes
    LaunchedEffect(state) {
        snapshotFlow { state.value }.collect {
            log.d { "ScreenSetup: State Changed -> $it" }
        }
    }

    libSetup()

    content()

    val str = stringResource(Res.string.press_again_to_exit)
    log.d { "ScreenSetup: Parent BackHandler Registered. Enabled=$isBackHandlingEnabled" }

    PlatformBackHandler(
        message = str,
        modifier = Modifier.padding(horizontal = 8.dp),
        isEnabled = isBackHandlingEnabled,
        onBackDoubleClick = onBackDoubleClick,
        content = ::SnackbarMessage
    )
}
