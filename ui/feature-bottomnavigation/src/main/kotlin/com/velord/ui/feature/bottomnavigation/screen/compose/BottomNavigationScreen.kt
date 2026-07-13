package com.velord.ui.feature.bottomnavigation.screen.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import com.velord.core.ui.util.ObserveSharedFlow
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigator
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationDestinationsUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import org.koin.androidx.compose.koinViewModel

private val log = Logger.withTag("LogBackStack")

@Composable
fun BottomNavigationScreen(navigator: BottomNavigator) {
    val viewModel = koinViewModel<BottomNavigationDestinationsVM>()

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

    ScreenSetup(viewModel = viewModel) {
        val uiState = viewModel.uiStateFlow.collectAsStateWithLifecycle()

        // Observe all clicks, not just state(state can't be changed when you click on same tab)
        ObserveSharedFlow(flow = viewModel.onTabClickEvent) { tab ->
            log.d { "BottomNavigationScreen onTabClickEvent: $tab" }
            navigator.onTabClick(tab)
        }

        Content(
            tab = uiState.value.tabState.current,
            createNavHost = {
                navigator.CreateNavHostForBottom(
                    modifier = Modifier
                        .padding(bottom = it.calculateBottomPadding())
                        .fillMaxSize(),
                    startRoute = uiState.value.tabState.current
                )
            },
            getNavigationItems = viewModel::getNavigationItems,
            onTabClick = { tab ->
                viewModel.onAction(BottomNavigationDestinationsUiAction.TabClick(tab))
            },
        )
    }
}
