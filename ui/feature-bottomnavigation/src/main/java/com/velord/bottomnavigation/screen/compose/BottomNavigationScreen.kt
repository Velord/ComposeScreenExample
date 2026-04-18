package com.velord.bottomnavigation.screen.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import com.velord.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import com.velord.core.ui.util.ObserveSharedFlow
import org.koin.androidx.compose.koinViewModel

private val log = Logger.withTag("LogBackStack")

@Composable
fun BottomNavigationScreen(navigator: BottomNavigator) {
    val viewModel = koinViewModel<BottomNavigationDestinationsVM>()

    navigator.SetupNavController(
        updateBackHandling = { startDestinationRoster, currentDestination ->
            viewModel.updateBackHandling(startDestinationRoster, currentDestination)
        },
        onTabChanged = { tab ->
            viewModel.onTabDestinationChanged(tab)
        }
    )

    ScreenSetup(viewModel = viewModel) {
        val currentTabState = viewModel.currentTabStateFlow.collectAsStateWithLifecycle()

        // Observe all clicks, not just state(state can't be changed when you click on same tab)
        ObserveSharedFlow(flow = viewModel.onTabClickEvent) { tab ->
            log.d { "BottomNavigationScreen onTabClickEvent: $tab" }
            navigator.onTabClick(tab)
        }

        Content(
            tab = currentTabState.value.current,
            createNavHost = {
                navigator.CreateNavHostForBottom(
                    modifier = Modifier
                        .padding(bottom = it.calculateBottomPadding())
                        .fillMaxSize(),
                    startRoute = currentTabState.value.current
                )
            },
            getNavigationItems = viewModel::getNavigationItems,
            onTabClick = viewModel::onTabClick,
        )
    }
}
