package com.velord.bottomnavigation.screen.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.velord.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import com.velord.bottomnavigation.viewmodel.BottomNavigationItem
import com.velord.bottomnavigation.viewmodel.TabState
import com.velord.core.ui.utils.ObserveSharedFlowAsState
import org.koin.androidx.compose.koinViewModel

interface BottomNavigator{
    fun onTabClick(tab: TabState)
    @Composable fun CreateNavHostForBottom(
        modifier: Modifier,
        startRoute: BottomNavigationItem
    )
    @Composable
    fun setupController(
        updateBackHandling: (startDestinationRoster: List<String?>, currentRoute: String?) -> Unit
    )
}

@Composable
fun BottomNavigationScreen(navigator: BottomNavigator) {
    val viewModel = koinViewModel<BottomNavigationDestinationsVM>()

    navigator.setupController { startDestinationRoster, currentDestination ->
        viewModel.updateBackHandling(startDestinationRoster, currentDestination)
    }

    ComposeContent(viewModel = viewModel) { currentTab ->
        ObserveSharedFlowAsState(flow = viewModel.currentTabFlow) { tab ->
            navigator.onTabClick(tab)
        }

        Content(
            tab = currentTab,
            createNavHost = {
                navigator.CreateNavHostForBottom(
                    modifier = Modifier
                        .padding(bottom = it.calculateBottomPadding())
                        .fillMaxSize(),
                    startRoute = currentTab
                )
            },
            getNavigationItems = viewModel::getNavigationItems,
            onTabClick = viewModel::onTabClick,
        )
    }
}