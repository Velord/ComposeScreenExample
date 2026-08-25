package com.velord.ui.feature.bottomnavigation.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import com.velord.core.resource.AppString
import com.velord.core.resource.stringResource
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.multiplebackstackapplier.utils.compose.SnackBarOnBackPressHandler
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem
import com.velord.ui.feature.bottomnavigation.screen.component.BottomBar
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationJetpackUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationJetpackVM

private val log = Logger.withTag(TAG)

@Composable
internal fun BottomNavigationJetpackScreen(viewModel: BottomNavigationJetpackVM) {
    val uiState = viewModel.uiStateFlow.collectAsStateWithLifecycle()

    BottomBar(
        selectedItem = uiState.value.tabState.current,
        navigationItemRoster = BottomNavigationItem.entries,
        onClick = { tab ->
            viewModel.onAction(BottomNavigationJetpackUiAction.TabClick(tab))
        },
    )

    log.d { "BottomNavScreen: ${uiState.value.backHandlingState}" }
    if (uiState.value.backHandlingState.isEnabled) {
        val str = stringResource(AppString.press_again_to_exit)
        SnackBarOnBackPressHandler(
            message = str,
            modifier = Modifier.padding(horizontal = 8.dp),
            enabled = uiState.value.backHandlingState.isEnabled,
            onBackClickLessThanDuration = {
                viewModel.onAction(BottomNavigationJetpackUiAction.BackDoubleClick)
            },
        ) {
            Snackbar {
                Text(text = it.visuals.message)
            }
        }
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    BottomBar(
        selectedItem = BottomNavigationItem.Camera,
        navigationItemRoster = BottomNavigationItem.entries,
        onClick = {},
    )
}
