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
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationBackBehavior
import org.jetbrains.compose.resources.stringResource

private val log = Logger.withTag("LogBackStack")

@Composable
internal fun <T> ScreenSetup(
    state: State<T>,
    backBehavior: BottomNavigationBackBehavior,
    onBackClick: () -> Unit,
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

    log.d { "ScreenSetup: BackHandler Registered. Behavior=$backBehavior" }

    val str = stringResource(Res.string.press_again_to_exit)
    PlatformBackHandler(
        backBehavior = backBehavior,
        message = str,
        modifier = Modifier.padding(horizontal = 8.dp),
        onBackClick = onBackClick,
        onBackDoubleClick = onBackDoubleClick,
        content = ::SnackbarMessage
    )
}
