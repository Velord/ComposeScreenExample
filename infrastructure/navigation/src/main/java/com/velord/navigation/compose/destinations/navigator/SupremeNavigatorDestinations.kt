package com.velord.navigation.compose.destinations.navigator

import android.annotation.SuppressLint
import android.util.Log
import androidx.collection.forEach
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
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
import com.velord.navigation.compose.vanilla.navigator.LogNavigationEvents
import com.velord.navigation.compose.vanilla.navigator.logTabClick

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

    private val bottomTabNavControllerState: MutableState<NavHostController?> = mutableStateOf(null)

    init {
        Log.d("LogBackStack - SupremeNavigatorDestinations", "init: ${this.supremeNavController}")
    }

    override fun onTabClick(tab: TabState) {
        val controller = bottomTabNavControllerState.value!!

        logTabClick(controller, tab)

        onTabClickDestinations(
            isSelected = tab.isSame,
            item = tab.current,
            destinationNavigator = controller.toDestinationsNavigator(),
            navigator = this,
        )
    }

    @Composable
    override fun CreateNavHostForBottom(
        modifier: Modifier,
        start: BottomNavigationItem
    ) {
        val navController = bottomTabNavControllerState.value!!

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

    @SuppressLint("RestrictedApi")
    @Composable
    override fun SetupNavController(
        updateBackHandling: (startDestinationRoster: List<String?>, currentRoute: String?) -> Unit,
        onTabChanged: (BottomNavigationItem) -> Unit,
    ) {
        val navController = rememberNavController()

        if (bottomTabNavControllerState.value != navController) {
            bottomTabNavControllerState.value = navController
        }

        LogNavigationEvents(navController = navController, label = "DestinationsLib")

        val bottomTabNavController = bottomTabNavControllerState.value
        val backStackEntry = bottomTabNavController!!.currentBackStackEntryAsState()
        val currentDestination = backStackEntry.value?.destination

        LaunchedEffect(currentDestination) {
            if (currentDestination == null) return@LaunchedEffect
            val startDestinationRoster = mutableListOf<String?>()

            bottomTabNavController.graph.nodes.forEach { _, value ->
                val startDestination = when (value) {
                    is NavGraph -> value.startDestinationRoute
                    else -> value.route
                }
                startDestinationRoster.add(startDestination)
            }
            updateBackHandling(
                startDestinationRoster,
                currentDestination.route
            )

            // Sync current tab state with nav controller destination
            BottomNavigationItem.entries.forEach { item ->
                val tabStartRoute = getTabStartRoute(item)
                // Check if current route matches this tab
                val isMatch = currentDestination.route?.contains(tabStartRoute::class.simpleName!!) == true
                if (isMatch) {
                    onTabChanged(item)
                }
            }
        }
    }

    override fun getRouteOnTabClick(route: BottomNavigationItem): Direction = when(route) {
        BottomNavigationItem.Camera -> CameraRecordingNavGraph
        BottomNavigationItem.Demo -> DemoNavGraph
        BottomNavigationItem.Setting -> BottomNavigationSettingsDestinationDestination
    }

    override fun getTabStartRoute(route: BottomNavigationItem): RouteOrDirection = when(route) {
        BottomNavigationItem.Camera -> CameraRecordingNavGraph.startRoute
        BottomNavigationItem.Demo -> DemoNavGraph.startRoute
        BottomNavigationItem.Setting -> BottomNavigationSettingsDestinationDestination
    }

    override fun getPopRouteOnTabClick(): NavHostGraphSpec = BottomNavigationNavGraph

    override fun goToSettingFromCameraRecording() {
        supremeNavController.toDestinationsNavigator().navigate(MainSettingsDestinationDestination)
    }
}