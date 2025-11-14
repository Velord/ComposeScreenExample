package com.velord.navigation.compose.vanilla.navigator

import android.util.Log
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.velord.bottomnavigation.screen.compose.BottomNavigator
import com.velord.bottomnavigation.viewmodel.BottomNavigationItem
import com.velord.bottomnavigation.viewmodel.TabState
import com.velord.camerarecording.CameraRecordingNavigator
import com.velord.navigation.compose.vanilla.GraphVanilla
import com.velord.navigation.compose.vanilla.graph.setupBottomNavigationGraph

internal class SupremeNavigatorVanilla(private val supremeNavController: NavHostController) :
    // BottomNavigationScreen setup
    BottomNavigator,
    // Bottom navigation tab click setup
    BottomTabNavigatorVanilla,
    // Below list of certain "Navigator" that work with supreme nav controller
    CameraRecordingNavigator {

    init {
        Log.d("LogBackStack - SupremeNavigatorVanilla", "init: ${this.supremeNavController}")
    }

    override fun onTabClick(tab: TabState, controller: NavHostController) {
        onTabClickVanilla(
            isSelected = tab.isSame,
            item = tab.current,
            navController = controller,
            navigator = this,
        )
    }

    @Composable
    override fun CreateNavHostForBottom(
        navController: NavHostController,
        modifier: Modifier,
        startRoute: BottomNavigationItem,
    ) {
        NavHost(
            navController = navController,
            startDestination = GraphVanilla.BottomTab.Demo.Self,
            modifier = modifier,
            popExitTransition = { fadeOut() }
        ) {
            // Just wrapper for strong type system.
            // Includes all "Navigator" that possible from bottom graph
            val navigator = BottomNavigatorVanilla(
                parent = this@SupremeNavigatorVanilla,
                navController = navController
            )
            setupBottomNavigationGraph(navigator)
        }
    }

    @Composable
    override fun createBottomNavHostController(): NavHostController = rememberNavController()

    @Composable
    override fun createStackEntryAsState(controller: NavController): State<NavBackStackEntry?> =
        controller.currentBackStackEntryAsState()

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

    override fun goToSettingFromCameraRecording() {
        supremeNavController.navigate(GraphVanilla.Main.SettingDestinationVanilla)
    }
}