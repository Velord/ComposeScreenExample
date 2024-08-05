package com.velord.bottomnavigation.screen.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.velord.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import com.velord.bottomnavigation.viewmodel.BottomNavigationItem
import com.velord.bottomnavigation.viewmodel.TabState
import com.velord.uicore.utils.ObserveSharedFlow
import org.koin.androidx.compose.koinViewModel

interface BottomNavigator{
    fun onTabClick(tab: TabState, controller: NavHostController)
    @Composable fun CreateNavHostForBottom(
        navController: NavHostController,
        modifier: Modifier,
        startRoute: BottomNavigationItem
    )
    @Composable fun createNavController(): NavHostController
    @Composable fun createStackEntryAsState(controller: NavController): State<NavBackStackEntry?>
}

@Composable
fun BottomNavigationScreen(navigator: BottomNavigator) {
    val viewModel = koinViewModel<BottomNavigationDestinationsVM>()

    val navController = navigator.createNavController()
    val backStackEntry = navigator.createStackEntryAsState(navController)
    val currentDestination = backStackEntry.value?.destination

    ComposeContent(
        viewModel = viewModel,
        navController = navController,
        currentDestination = currentDestination,
    ) { currentTab ->
        ObserveSharedFlow(flow = viewModel.currentTabFlow) { tab ->
            navigator.onTabClick(tab, navController)
        }

        Content(
            tab = currentTab,
            createNavHost = {
                navigator.CreateNavHostForBottom(
                    navController = navController,
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