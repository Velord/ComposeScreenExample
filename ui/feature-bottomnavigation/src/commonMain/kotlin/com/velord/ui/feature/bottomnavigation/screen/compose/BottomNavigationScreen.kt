package com.velord.ui.feature.bottomnavigation.screen.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.core.ui.util.ObserveSharedFlow
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigator
import com.velord.ui.feature.bottomnavigation.screen.component.BottomNavigationContent
import com.velord.ui.feature.bottomnavigation.screen.component.ScreenSetup
import com.velord.ui.feature.bottomnavigation.viewmodel.destinations.BottomNavigationDestinationsUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.destinations.BottomNavigationDestinationsVM
import org.koin.compose.viewmodel.koinViewModel

private val log = Logger.withTag("LogBackStack")

@Composable
fun BottomNavigationScreen(navigator: BottomNavigator) {
    val viewModel = koinViewModel<BottomNavigationDestinationsVM>()

    val uiState = viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val isEnabledState = remember {
        derivedStateOf { uiState.value.backHandlingState.isEnabled }
    }

    ScreenSetup(
        state = uiState,
        isBackHandlingEnabled = isEnabledState.value,
        onBackDoubleClick = {
            viewModel.onAction(BottomNavigationDestinationsUiAction.BackDoubleClick)
        },
        libSetup = {
            navigator.SetupNavController(
                updateBackHandling = { startDestinationRoster, currentDestination ->
                    viewModel.onAction(
                        BottomNavigationDestinationsUiAction.UpdateBackHandling(
                            startDestinationRoster = startDestinationRoster,
                            currentRoute = currentDestination,
                        )
                    )
                },
                onTabChanged = { tab ->
                    viewModel.onAction(BottomNavigationDestinationsUiAction.TabDestinationChanged(tab))
                }
            )
            // What if we want to not block system back and allow just exit the app ?
            // In that case, we can simply not call graphCompletedHandling() at all,
            // and the back handling will not be enabled,
            // allowing the system to handle the back press as usual.
            SideEffect {
                // When the graph is completed, we can proceed with the back handling
                // Current logic is simple, we just allow the back handling
                // viewModel.graphCompletedHandling()
            }
        },
    ) {
        // Observe all clicks, not just state(state can't be changed when you click on same tab)
        ObserveSharedFlow(flow = viewModel.onTabClickEvent) { tab ->
            log.d { "BottomNavigationScreen onTabClickEvent: $tab" }
            navigator.onTabClick(tab)
        }

        BottomNavigationContent(
            selectedItem = uiState.value.tabState.current,
            navigationItemRoster = viewModel.getNavigationItems(),
            onClick = { tab ->
                viewModel.onAction(BottomNavigationDestinationsUiAction.TabClick(tab))
            },
            content = {
                navigator.CreateNavHostForBottom(
                    modifier = Modifier
                        .padding(bottom = it.calculateBottomPadding())
                        .fillMaxSize(),
                    startRoute = uiState.value.tabState.current
                )
            },
        )
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    BottomNavigationContent(
        selectedItem = BottomNavigationItem.Camera,
        navigationItemRoster = BottomNavigationItem.entries,
        onClick = {},
        content = {},
    )
}
