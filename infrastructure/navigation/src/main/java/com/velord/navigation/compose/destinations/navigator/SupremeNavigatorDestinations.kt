package com.velord.navigation.compose.destinations.navigator

import android.util.Log
import androidx.collection.forEach
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.navigation.destinations.BottomNavigationSettingsDestinationDestination
import com.ramcosta.composedestinations.generated.navigation.destinations.MainSettingsDestinationDestination
import com.ramcosta.composedestinations.generated.navigation.navgraphs.BottomNavigationNavGraph
import com.ramcosta.composedestinations.generated.navigation.navgraphs.CameraRecordingNavGraph
import com.ramcosta.composedestinations.generated.navigation.navgraphs.DemoNavGraph
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.NavHostGraphSpec
import com.ramcosta.composedestinations.spec.RouteOrDirection
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import com.velord.bottomnavigation.screen.compose.BottomNavigator
import com.velord.bottomnavigation.viewmodel.BottomNavigationItem
import com.velord.bottomnavigation.viewmodel.TabState
import com.velord.camerarecording.CameraRecordingNavigator
import com.velord.navigation.compose.destinations.transition.PopFadeTransition

/*
* !!!
* It is crucial to use generated classes
* !!!
*/
internal class SupremeNavigatorDestinations(private val supremeNavController: NavHostController) :
// BottomNavigationScreen setup
    BottomNavigator,
    // Bottom navigation tab click setup. It depends on the specific nav library.
    BottomTabNavigatorDestinations,
    // Below list of certain "Navigator" that work with supreme nav controller
    CameraRecordingNavigator {

    private var bottomTabNavController: NavHostController? = null

    init {
        Log.d("LogBackStack - SupremeNavigatorDestinations", "init: ${this.supremeNavController}")
    }

    override fun onTabClick(tab: TabState) {
        onTabClickDestinations(
            isSelected = tab.isSame,
            item = tab.current,
            destinationNavigator = bottomTabNavController!!.toDestinationsNavigator(),
            navigator = this,
        )
    }

    @Composable
    override fun CreateNavHostForBottom(
        modifier: Modifier,
        start: BottomNavigationItem
    ) {
        val navController = bottomTabNavController!!
        DestinationsNavHost(
            navGraph = BottomNavigationNavGraph,
            modifier = modifier,
            //start = getDirection(start), // Fixme: this is not working
            defaultTransitions = PopFadeTransition,
            navController = navController,
            dependenciesContainerBuilder = {
                // Just wrapper for strong type system.
                // Includes all "Navigator" that possible from bottom graph
                val navigator = BottomNavigatorDestinations(
                    parent = this@SupremeNavigatorDestinations,
                    navController = navController,
                )
                dependency(navigator)
            }
        )
    }

    @Composable
    override fun setupController(
        updateBackHandling: (startDestinationRoster: List<String?>, currentRoute: String?) -> Unit
    ) {
        bottomTabNavController = rememberNavController()
        val backStackEntry = bottomTabNavController!!.currentBackStackEntryAsState()
        val currentDestination = backStackEntry.value?.destination

        LaunchedEffect(currentDestination) {
            if (currentDestination == null) return@LaunchedEffect
            val nodes = mutableListOf<NavDestination>()

            bottomTabNavController!!.graph.nodes.forEach { _, value ->
                nodes.add(value)
            }
            val startDestinationRoster = nodes.map {
                when (it) {
                    is NavGraph -> it.startDestinationRoute
                    else -> it.route
                }
            }
            updateBackHandling(
                startDestinationRoster,
                currentDestination.route
            )
        }
    }

    override fun getDirection(route: BottomNavigationItem): Direction = when(route) {
        BottomNavigationItem.Camera -> CameraRecordingNavGraph
        BottomNavigationItem.Demo -> DemoNavGraph
        BottomNavigationItem.Setting -> BottomNavigationSettingsDestinationDestination
    }

    override fun getGraph(): NavHostGraphSpec = BottomNavigationNavGraph

    override fun getStartRoute(route: BottomNavigationItem): RouteOrDirection = when(route) {
        BottomNavigationItem.Camera -> CameraRecordingNavGraph.startRoute
        BottomNavigationItem.Demo -> DemoNavGraph.startRoute
        BottomNavigationItem.Setting -> BottomNavigationSettingsDestinationDestination
    }

    override fun goToSettingFromCameraRecording() {
        supremeNavController.toDestinationsNavigator().navigate(MainSettingsDestinationDestination)
    }
}