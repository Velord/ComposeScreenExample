package com.velord.navigation.compose.nav3.navigator

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.velord.bottomnavigation.screen.compose.BottomNavigator
import com.velord.bottomnavigation.viewmodel.BottomNavigationItem
import com.velord.bottomnavigation.viewmodel.TabState
import com.velord.camerarecording.CameraRecordingNavigator
import com.velord.navigation.compose.nav3.GraphNav3
import com.velord.navigation.compose.nav3.graph.setupBottomNavigationNav3

private val BOTTOM_TAB_TOP_LEVEL_ROUTES = setOf(
    GraphNav3.BottomTab.CameraRecording.CameraRecordingDestinationNav3,
    GraphNav3.BottomTab.Demo.DemoDestinationNav3,
    GraphNav3.BottomTab.SettingDestinationNav3
)

internal class SupremeNavigatorNav3(private val backStack: SnapshotStateList<NavKey>) :
    // BottomNavigationScreen setup
    BottomNavigator,
    // Bottom navigation tab click setup. It depends on the specific nav library.
    //BottomTabNavigatorNav3,
    // Below list of certain "Navigator" that work with supreme nav controller
    CameraRecordingNavigator {

    private var backStackNavigator: BackStackNavigator? = null

    init {
        Log.d("LogBackStack - SupremeNavigatorNav3", "init: ${this.backStack}")
    }

    private fun getRouteOnTabClickNav3(route: BottomNavigationItem): NavKey = when(route) {
        BottomNavigationItem.Camera -> GraphNav3.BottomTab.CameraRecording.CameraRecordingDestinationNav3
        BottomNavigationItem.Demo -> GraphNav3.BottomTab.Demo.DemoDestinationNav3
        BottomNavigationItem.Setting -> GraphNav3.BottomTab.SettingDestinationNav3
    }

    private fun getTabStartRouteNav3(route: BottomNavigationItem): NavKey = when(route) {
        BottomNavigationItem.Camera -> GraphNav3.BottomTab.CameraRecording.CameraRecordingDestinationNav3
        BottomNavigationItem.Demo -> GraphNav3.BottomTab.Demo.DemoDestinationNav3
        BottomNavigationItem.Setting -> GraphNav3.BottomTab.SettingDestinationNav3
    }

    override fun onTabClick(tab: TabState) {
        onTabClickNav3(
            isSelected = tab.isSame,
            item = tab.current,
            backStackNavigator = backStackNavigator!!,
        )
    }

    internal fun onTabClickNav3(
        isSelected: Boolean,
        item: BottomNavigationItem,
        backStackNavigator: BackStackNavigator,
    ) {
        if (isSelected) {
            // When we click again on a bottom bar item and it was already selected
            // we want to pop the back stack until the initial destination of this bottom bar item
            val start = getTabStartRouteNav3(item)
            backStackNavigator.goBack()
            return
        }

        val direction = getRouteOnTabClickNav3(item)
        backStackNavigator.navigate(direction)
    }

    @Composable
    override fun CreateNavHostForBottom(
        modifier: Modifier,
        startRoute: BottomNavigationItem,
    ) {
        val navigationState = rememberNavigationState(
            startRoute = GraphNav3.BottomTab.Demo.DemoDestinationNav3,
            topLevelRoutes = BOTTOM_TAB_TOP_LEVEL_ROUTES
        )
        backStackNavigator = remember {
            BackStackNavigator(navigationState)
        }
        val navigator = remember {
            BottomNavigatorNav3(
                parent = this,
                backStackNavigator = backStackNavigator!!
            )
        }

        val entryProvider = entryProvider {
            setupBottomNavigationNav3(navigator)
        }
        NavDisplay(
            entries = navigationState.toEntries(entryProvider),
            onBack = { backStackNavigator?.goBack() },
        )
    }

    @Composable
    override fun setupController(
        updateBackHandling: (startDestinationRoster: List<String?>, currentRoute: String?) -> Unit
    ) {
        // TODO:
    }

    override fun goToSettingFromCameraRecording() {
        backStack.add(GraphNav3.Main.SettingDestinationNav3)
    }
}