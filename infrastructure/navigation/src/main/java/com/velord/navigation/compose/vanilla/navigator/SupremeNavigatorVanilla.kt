package com.velord.navigation.compose.vanilla.navigator

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
import com.velord.navigation.compose.vanilla.CameraRecordingDestinationVanilla
import com.velord.navigation.compose.vanilla.CameraRecordingGraphVanilla
import com.velord.navigation.compose.vanilla.CameraRecordingSettingsDestinationVanilla
import com.velord.navigation.compose.vanilla.DemoDestinationVanilla
import com.velord.navigation.compose.vanilla.SettingsDestinationVanilla
import com.velord.navigation.compose.vanilla.graph.setupBottomNavigationGraph

internal class SupremeNavigatorVanilla(
    private val supremeNavController: NavHostController
) : BottomNavigator, BottomTabNavigatorVanilla, CameraRecordingNavigator {

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
            startDestination = DemoDestinationVanilla,
            modifier = modifier,
            popExitTransition = { fadeOut() }
        ) {
            val navigator = BottomNavigatorVanilla(this@SupremeNavigatorVanilla, navController)
            setupBottomNavigationGraph(navigator)
        }
    }

    @Composable
    override fun createNavController(): NavHostController = rememberNavController()

    @Composable
    override fun createStackEntryAsState(controller: NavController): State<NavBackStackEntry?> =
        controller.currentBackStackEntryAsState()

    override fun getRoute(route: BottomNavigationItem): Any = when(route) {
        BottomNavigationItem.Camera -> CameraRecordingGraphVanilla
        BottomNavigationItem.Demo -> DemoDestinationVanilla
        BottomNavigationItem.Settings -> SettingsDestinationVanilla
    }

    override fun getStartRoute(route: BottomNavigationItem): Any = when(route) {
        BottomNavigationItem.Camera -> CameraRecordingDestinationVanilla
        BottomNavigationItem.Demo -> DemoDestinationVanilla
        BottomNavigationItem.Settings -> SettingsDestinationVanilla
    }

    override fun goToSettingsFromCameraRecording() {
        supremeNavController.navigate(CameraRecordingSettingsDestinationVanilla)
    }
}