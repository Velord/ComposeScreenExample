package com.velord.navigation.compose.vanilla.navigator

import android.annotation.SuppressLint
import android.util.Log
import androidx.collection.forEach
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph
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

    private var bottomTabNavController: NavHostController? = null

    init {
        Log.d("LogBackStack - SupremeNavigatorVanilla", "init: ${this.supremeNavController}")
    }

    override fun onTabClick(tab: TabState) {
        onTabClickVanilla(
            isSelected = tab.isSame,
            item = tab.current,
            navController = bottomTabNavController!!,
            navigator = this,
        )
    }

    @Composable
    override fun CreateNavHostForBottom(
        modifier: Modifier,
        startRoute: BottomNavigationItem,
    ) {
        NavHost(
            navController = bottomTabNavController!!,
            startDestination = GraphVanilla.BottomTab.Demo.Self,
            modifier = modifier,
            popExitTransition = { fadeOut() }
        ) {
            // Just wrapper for strong type system.
            // Includes all "Navigator" that possible from bottom graph
            val navigator = BottomNavigatorVanilla(
                parent = this@SupremeNavigatorVanilla,
                navController = bottomTabNavController!!
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
        bottomTabNavController = rememberNavController()
        val backStackEntry = bottomTabNavController!!.currentBackStackEntryAsState()
        val currentDestination = backStackEntry.value?.destination

        LaunchedEffect(currentDestination) {
            Log.d("@@@", "SetupNavController: $currentDestination")
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
                val tabStartRoute = getTabStartRouteVanilla(item)
                // Check if current route matches this tab
                val isMatch = currentDestination.route?.contains(tabStartRoute::class.simpleName!!) == true
                if (isMatch) {
                    Log.d("@@@", "SetupNavController match")
                    onTabChanged(item)
                }
            }
        }
    }

    override fun getRouteOnTabClickVanilla(route: BottomNavigationItem): Any = when(route) {
        BottomNavigationItem.Camera -> GraphVanilla.BottomTab.CameraRecording.Self
        BottomNavigationItem.Demo -> GraphVanilla.BottomTab.Demo.Self
        BottomNavigationItem.Setting -> GraphVanilla.BottomTab.SettingDestinationVanilla
    }

    override fun getTabStartRouteVanilla(route: BottomNavigationItem): Any = when(route) {
        BottomNavigationItem.Camera -> GraphVanilla.BottomTab.CameraRecording.CameraRecordingDestinationVanilla
        BottomNavigationItem.Demo -> GraphVanilla.BottomTab.Demo.DemoDestinationVanilla
        BottomNavigationItem.Setting -> GraphVanilla.BottomTab.SettingDestinationVanilla
    }

    override fun goToSettingFromCameraRecording() {
        supremeNavController.navigate(GraphVanilla.Main.SettingDestinationVanilla)
    }
}