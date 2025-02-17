package com.velord.navigation.compose.destinations.navigator

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.navigation.destinations.BottomNavigationSettingsDestinationDestination
import com.ramcosta.composedestinations.generated.navigation.destinations.DemoDestinationDestination
import com.ramcosta.composedestinations.generated.navigation.destinations.MainSettingsDestinationDestination
import com.ramcosta.composedestinations.generated.navigation.navgraphs.BottomNavigationNavGraph
import com.ramcosta.composedestinations.generated.navigation.navgraphs.CameraRecordingNavGraph
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.NavHostGraphSpec
import com.ramcosta.composedestinations.spec.RouteOrDirection
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import com.velord.bottomnavigation.screen.compose.BottomNavigator
import com.velord.bottomnavigation.viewmodel.BottomNavigationItem
import com.velord.bottomnavigation.viewmodel.TabState
import com.velord.camerarecording.CameraRecordingNavigator

internal class SupremeNavigatorDestinations(
    private val supremeNavController: NavHostController
) : BottomNavigator, BottomTabNavigatorDestinations, CameraRecordingNavigator {

    init {
        Log.d("LogBackStack - SupremeNavigator", "init: ${this.supremeNavController}")
    }

    override fun goToSettingsFromCameraRecording() {
        supremeNavController.toDestinationsNavigator().navigate(MainSettingsDestinationDestination)
    }

    override fun getDirection(route: BottomNavigationItem): Direction = when(route) {
        BottomNavigationItem.Camera -> CameraRecordingNavGraph
        BottomNavigationItem.Demo -> DemoDestinationDestination
        BottomNavigationItem.Settings -> BottomNavigationSettingsDestinationDestination
    }

    override fun getGraph(): NavHostGraphSpec = BottomNavigationNavGraph

    override fun onTabClick(tab: TabState, controller: NavHostController) {
        onTabClickDestinations(
            isSelected = tab.isSame,
            item = tab.current,
            destinationNavigator = controller.toDestinationsNavigator(),
            navigator = this,
        )
    }

    @Composable
    override fun CreateNavHostForBottom(
        navController: NavHostController,
        modifier: Modifier,
        start: BottomNavigationItem
    ) {
        DestinationsNavHost(
            navGraph = BottomNavigationNavGraph,
            modifier = modifier,
            //start = getDirection(start), // Fixme: this is not working
            navController = navController,
            dependenciesContainerBuilder = {
                val navigator = BottomNavigatorDestinations(
                    navController = navController,
                    parent = this@SupremeNavigatorDestinations
                )
                dependency(navigator)
            }
        )
    }

    @Composable
    override fun createNavController(): NavHostController {
        val navController = rememberNavController()
        return navController
    }

    @Composable
    override fun createStackEntryAsState(controller: NavController): State<NavBackStackEntry?> {
        return controller.currentBackStackEntryAsState()
    }

    override fun getStartRoute(route: BottomNavigationItem): RouteOrDirection = when(route) {
        BottomNavigationItem.Camera -> CameraRecordingNavGraph.startRoute
        BottomNavigationItem.Demo -> DemoDestinationDestination
        BottomNavigationItem.Settings -> BottomNavigationSettingsDestinationDestination
    }
}