package com.velord.navigation.compose.destinations

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.destinations.BottomNavigationSettingsDestinationDestination
import com.ramcosta.composedestinations.generated.destinations.CameraRecordingSettingsDestinationDestination
import com.ramcosta.composedestinations.generated.destinations.DemoDestinationDestination
import com.ramcosta.composedestinations.generated.destinations.FlowSummatorDestinationDestination
import com.ramcosta.composedestinations.generated.destinations.HintPhoneNumberDestinationDestination
import com.ramcosta.composedestinations.generated.destinations.ModifierDemoDestinationDestination
import com.ramcosta.composedestinations.generated.destinations.MorphDemoDestinationDestination
import com.ramcosta.composedestinations.generated.destinations.MovieDestinationDestination
import com.ramcosta.composedestinations.generated.destinations.ShapeDemoDestinationDestination
import com.ramcosta.composedestinations.generated.navgraphs.BottomNavigationNavGraph
import com.ramcosta.composedestinations.generated.navgraphs.CameraRecordingNavGraph
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.NavHostGraphSpec
import com.ramcosta.composedestinations.spec.RouteOrDirection
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import com.velord.bottomnavigation.screen.BottomNavigationItem
import com.velord.bottomnavigation.screen.BottomNavigator
import com.velord.camerarecording.CameraRecordingNavigator
import com.velord.feature.demo.DemoDest
import com.velord.feature.demo.DemoNavigator

internal class SupremeNavigator(
    private val navController: NavHostController
) : BottomNavigator, DemoNavigator, CameraRecordingNavigator {

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
                dependency(SupremeNavigator(navController = navController))
            }
        )
    }

    override fun goTo(dest: DemoDest) {
        val dest = when(dest) {
            DemoDest.Shape -> ShapeDemoDestinationDestination
            DemoDest.Modifier -> ModifierDemoDestinationDestination
            DemoDest.FlowSummator -> FlowSummatorDestinationDestination
            DemoDest.Morph -> MorphDemoDestinationDestination
            DemoDest.HintPhoneNumber -> HintPhoneNumberDestinationDestination
            DemoDest.Movie -> MovieDestinationDestination
        }
        navController.toDestinationsNavigator().navigate(dest)
    }

    override fun goToSettingsFromCameraRecording() {
        navController.toDestinationsNavigator().navigate(CameraRecordingSettingsDestinationDestination)
    }

    override fun getStartRoute(route: BottomNavigationItem): RouteOrDirection = when(route) {
        BottomNavigationItem.Camera -> CameraRecordingNavGraph.startRoute
        BottomNavigationItem.Demo -> DemoDestinationDestination
        BottomNavigationItem.Settings -> BottomNavigationSettingsDestinationDestination
    }
}