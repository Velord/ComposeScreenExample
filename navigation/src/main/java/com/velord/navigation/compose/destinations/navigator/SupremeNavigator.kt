package com.velord.navigation.compose.destinations.navigator

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.destinations.BottomNavigationSettingsDestinationDestination
import com.ramcosta.composedestinations.generated.destinations.DemoDestinationDestination
import com.ramcosta.composedestinations.generated.destinations.MainSettingsDestinationDestination
import com.ramcosta.composedestinations.generated.navgraphs.BottomNavigationNavGraph
import com.ramcosta.composedestinations.generated.navgraphs.CameraRecordingNavGraph
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.NavHostGraphSpec
import com.ramcosta.composedestinations.spec.RouteOrDirection
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import com.velord.bottomnavigation.screen.compose.BottomNavigationItem
import com.velord.bottomnavigation.screen.compose.BottomNavigator
import com.velord.camerarecording.CameraRecordingNavigator

internal class SupremeNavigator(
    private val navController: NavHostController
) : CameraRecordingNavigator, BottomNavigator {

    init {
        Log.d("LogBackStack - SupremeNavigator", "init: ${this.navController}")
    }

    override fun goToSettingsFromCameraRecording() {
        navController.toDestinationsNavigator().navigate(MainSettingsDestinationDestination)
    }

    override fun getDirection(route: BottomNavigationItem): Direction = when(route) {
        BottomNavigationItem.Camera -> CameraRecordingNavGraph
        BottomNavigationItem.Demo -> DemoDestinationDestination
        BottomNavigationItem.Settings -> BottomNavigationSettingsDestinationDestination
    }

    override fun getGraph(): NavHostGraphSpec = BottomNavigationNavGraph

    @Composable
    override fun CreateDestinationsNavHostForBottom(
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
                val navigator = BottomNavigator(
                    navController = navController,
                    parent = this@SupremeNavigator
                )
                dependency(navigator)
            }
        )
    }

    override fun getStartRoute(route: BottomNavigationItem): RouteOrDirection = when(route) {
        BottomNavigationItem.Camera -> CameraRecordingNavGraph.startRoute
        BottomNavigationItem.Demo -> DemoDestinationDestination
        BottomNavigationItem.Settings -> BottomNavigationSettingsDestinationDestination
    }
}