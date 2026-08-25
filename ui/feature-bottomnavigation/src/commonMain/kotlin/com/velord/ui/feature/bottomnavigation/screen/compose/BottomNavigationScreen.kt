package com.velord.ui.feature.bottomnavigation.screen.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.core.ui.util.ObserveSharedFlow
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigator
import com.velord.ui.feature.bottomnavigation.screen.component.Content
import com.velord.ui.feature.bottomnavigation.screen.component.ScreenSetup
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationVM
import org.koin.compose.viewmodel.koinViewModel

private val log = Logger.withTag("LogBackStack")

@Composable
fun BottomNavigationScreen(navigator: BottomNavigator) {
    val viewModel = koinViewModel<BottomNavigationVM>()

    val uiState = viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val backBehavior = uiState.value.backBehavior

    ScreenSetup(
        state = uiState,
        backBehavior = backBehavior,
        onBackClick = {
            viewModel.onAction(BottomNavigationUiAction.BackRequest)
        },
        onBackDoubleClick = {
            viewModel.onAction(BottomNavigationUiAction.BackDoubleClick)
        },
        libSetup = {
            navigator.SetupNavController(
                updateBackHandling = { startDestinationRoster, currentDestination ->
                    viewModel.onAction(
                        BottomNavigationUiAction.UpdateBackHandling(
                            startDestinationRoster = startDestinationRoster,
                            currentRoute = currentDestination,
                        )
                    )
                },
                onTabChanged = { tab ->
                    viewModel.onAction(
                        BottomNavigationUiAction.TabDestinationChanged(tab)
                    )
                }
            )
            // What if we want to not block system back and allow just exit the app ?
            // In that case, we can simply not call graphCompletedHandling() at all,
            // and the back handling will not be enabled,
            // allowing the system to handle the back press as usual.
        },
    ) {
        // Observe all clicks, not just state(state can't be changed when you click on same tab)
        ObserveSharedFlow(flow = viewModel.onTabClickEvent) { tab ->
            log.d { "BottomNavigationScreen onTabClickEvent: $tab" }
            navigator.onTabClick(tab)
        }

        Content(
            selectedItem = uiState.value.tabState.current,
            navigationItemRoster = viewModel.getNavigationItemRoster(),
            onTabClick = { tab ->
                viewModel.onAction(BottomNavigationUiAction.TabClick(tab))
            },
            content = {
                navigator.CreateNavHostForBottom(
                    modifier = Modifier
                        .padding(bottom = it.calculateBottomPadding())
                        .fillMaxSize(),
                    startRoute = uiState.value.tabState.current,
                )
            },
        )
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    Content(
        selectedItem = BottomNavigationItem.Camera,
        navigationItemRoster = BottomNavigationItem.entries,
        onTabClick = {},
        content = {},
    )
}
