package com.velord.navigation.compose.vanilla.navigator

import android.annotation.SuppressLint
import android.util.Log
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
import com.velord.bottomnavigation.screen.compose.BottomNavigator
import com.velord.bottomnavigation.viewmodel.BottomNavigationItem
import com.velord.bottomnavigation.viewmodel.TabState
import com.velord.camerarecording.CameraRecordingNavigator
import com.velord.navigation.compose.vanilla.GraphVanilla
import com.velord.navigation.compose.vanilla.graph.setupBottomNavigationGraphVanilla

internal class SupremeNavigatorVanilla(private val supremeNavController: NavHostController) :
    // BottomNavigationScreen setup
    BottomNavigator,
    // Bottom navigation tab click setup. It depends on the specific nav library.
    BottomTabNavigatorVanilla,
    // Below list of certain "Navigator" that work with supreme nav controller
    CameraRecordingNavigator {

    private val bottomTabNavControllerState: MutableState<NavHostController?> = mutableStateOf(null)

    init {
        Log.d("LogBackStack - SupremeNavigatorVanilla", "init: ${this.supremeNavController}")
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

        LogNavigationEvents(navController = navController, label = "BottomTab")

        val bottomTabNavController = bottomTabNavControllerState.value
        val backStackEntry = bottomTabNavController!!.currentBackStackEntryAsState()
        val currentDestination = backStackEntry.value?.destination

        LaunchedEffect(currentDestination) {
            if (currentDestination == null) return@LaunchedEffect
            val startDestinationRoster = mutableListOf<String?>()

            bottomTabNavController!!.graph.nodes.forEach { _, value ->
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

        // 1. If no current entry (App Start), default to the Global Start (Demo)
        if (currentEntry == null) {
            return rootGraph.findStartDestination().id
        }

        // 2. Dynamic Logic: Find the root of the CURRENT tab
        var tabRoot = currentEntry.destination

        // Walk up the graph until we find the direct child of the Root NavHost
        // (e.g., Walk from "ProfileDetail" -> "ProfileGraph" -> "RootNavGraph")
        // We want "ProfileGraph".
        while (tabRoot.parent != null && tabRoot.parent!!.id != rootGraph.id) {
            tabRoot = tabRoot.parent!!
        }

        return tabRoot.id
    }

    override fun goToSettingFromCameraRecording() {
        supremeNavController.navigate(GraphVanilla.Main.SettingDestinationVanilla)
    }
}

internal fun logTabClick(controller: NavHostController, tab: TabState) {
    val graphStartId = controller.graph.startDestinationId
    val foundStartId = controller.graph.findStartDestination().id
    val currentDest = controller.currentDestination?.route
    val startName = controller.graph.findNode(foundStartId)?.route ?: "Unknown ID: $foundStartId"
    Log.d("LogBackStack", """
            --- ON TAB CLICK (${tab.current}) ---
            Current Location: $currentDest
            Root Graph Start ID: $graphStartId
            FindStartDestination: $startName
            -------------------------------------
        """.trimIndent())
}

@Composable
internal fun LogNavigationEvents(navController: NavHostController, label: String) {
    LaunchedEffect(navController) {
        val flow = navController.currentBackStackEntryFlow
        flow.collect { entry ->
            val stack = navController.currentBackStack.value
            val sb = StringBuilder()
            sb.append("\n--- NAV STATE CHANGE ($label) ---\n")
            sb.append("Current Dest: ${entry.destination.route}\n")
            sb.append("Stack Size: ${stack.size}\n")
            stack.forEachIndexed { index, backStackEntry ->
                sb.append("  [$index] ${backStackEntry.destination.route} \n")
                sb.append("      (ID: ${backStackEntry.id.subSequence(0, 8)}...) \n")
                sb.append("      (Lifecycle: ${backStackEntry.lifecycle.currentState})\n")
            }
            sb.append("--------------------------------\n")
            Log.d("LogBackStack", sb.toString())
        }
    }
}