package com.velord.infrastructure.navigation.compose.vanilla.navigator

import android.annotation.SuppressLint
import androidx.collection.forEach
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger
import com.velord.infrastructure.navigation.compose.log.LogNavigationEvents
import com.velord.infrastructure.navigation.compose.log.logTabClick
import com.velord.infrastructure.navigation.compose.vanilla.GraphVanilla
import com.velord.infrastructure.navigation.compose.vanilla.graph.setupBottomNavigationGraphVanilla
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigator
import com.velord.ui.feature.bottomnavigation.navigation.TabState
import com.velord.ui.feature.camerarecording.CameraRecordingNavigator

private val vanillaLog = Logger.withTag("LogBackStack - SupremeNavigatorVanilla")

internal class SupremeNavigatorVanilla(private val supremeNavController: NavHostController) :
    // BottomNavigationScreen setup
    BottomNavigator,
    // Bottom navigation tab click setup. It depends on the specific nav library.
    BottomTabNavigatorVanilla,
    // Below list of certain "Navigator" that work with supreme nav controller
    CameraRecordingNavigator {

    private val bottomTabNavControllerState: MutableState<NavHostController?> = mutableStateOf(null)

    init {
        vanillaLog.d { "init: ${this.supremeNavController}" }
    }

    override fun onTabClick(tab: TabState) {
        val controller = bottomTabNavControllerState.value!!

        logTabClick(controller = controller, tab = tab)

        onTabClickVanilla(
            isSelected = tab.isSame,
            item = tab.current,
            navController = controller,
            navigator = this,
        )
    }

    @Composable
    override fun CreateNavHostForBottom(
        modifier: Modifier,
        startRoute: BottomNavigationItem,
    ) {
        val controller = bottomTabNavControllerState.value
        NavHost(
            navController = controller!!,
            startDestination = GraphVanilla.BottomTab.Demo.Self,
            modifier = modifier,
            popExitTransition = { fadeOut() }
        ) {
            // Just wrapper for strong type system.
            // Includes all "Navigator" that possible from bottom graph
            val navigator = BottomNavigatorVanilla(
                parent = this@SupremeNavigatorVanilla,
                navController = controller
            )
            setupBottomNavigationGraphVanilla(navigator)
        }
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

        LogNavigationEvents(
            navController = navController,
            label = "BottomTab",
        )

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
                val tabRouteName = tabStartRoute::class.simpleName!!
                val isMatch = currentDestination.route?.contains(tabRouteName) == true
                if (isMatch) {
                    onTabChanged(item)
                }
            }
        }
    }

    override fun getRouteOnTabClick(route: BottomNavigationItem): Any = when(route) {
        BottomNavigationItem.Camera -> GraphVanilla.BottomTab.CameraRecording.Self
        BottomNavigationItem.Demo -> GraphVanilla.BottomTab.Demo.Self
        BottomNavigationItem.Setting -> GraphVanilla.BottomTab.SettingDestinationVanilla
    }

    override fun getTabStartRoute(route: BottomNavigationItem): Any = when(route) {
        BottomNavigationItem.Camera -> GraphVanilla.BottomTab.CameraRecording.CameraRecordingDestinationVanilla
        BottomNavigationItem.Demo -> GraphVanilla.BottomTab.Demo.DemoDestinationVanilla
        BottomNavigationItem.Setting -> GraphVanilla.BottomTab.SettingDestinationVanilla
    }

    override fun getPopRouteOnTabClick(): Int {
        val controller = bottomTabNavControllerState.value ?: return 0

        val currentEntry = controller.currentBackStackEntry
        val rootGraph = controller.graph

        // If no current entry (App Start), default to the Global Start (Demo)
        if (currentEntry == null) {
            return rootGraph.findStartDestination().id
        }

        // Dynamic Logic: Find the root of the CURRENT tab
        var tabRoot = currentEntry.destination

        // Walk up the graph until we find the direct child of the Root NavHost
        // (e.g., Walk from "ProfileDetail" -> "ProfileGraph" -> "RootNavGraph")
        // We want "ProfileGraph".
        while (tabRoot.parent != null && tabRoot.parent!!.id != rootGraph.id) {
            tabRoot = tabRoot.parent!!
        }

        return tabRoot.id
    }

    internal fun goBack() {
        supremeNavController.popBackStack()
    }

    override fun goToSettingFromCameraRecording() {
        supremeNavController.navigate(GraphVanilla.Main.SettingDestinationVanilla)
    }
}
