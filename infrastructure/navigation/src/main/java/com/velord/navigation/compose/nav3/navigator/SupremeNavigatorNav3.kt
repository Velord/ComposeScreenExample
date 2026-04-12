package com.velord.navigation.compose.nav3.navigator

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
import com.velord.navigation.compose.nav3.graph.setupBottomNavigationGraphNav3

private val BOTTOM_TAB_TOP_LEVEL_ROUTES = setOf(
    GraphNav3.BottomTab.CameraRecording.CameraRecordingDestinationNav3,
    GraphNav3.BottomTab.Demo.DemoDestinationNav3,
    GraphNav3.BottomTab.SettingDestinationNav3
)

internal class SupremeNavigatorNav3(private val backStack: SnapshotStateList<NavKey>) :
    // BottomNavigationScreen setup
    BottomNavigator,
    // Below list of certain "Navigator" that work with supreme nav controller
    CameraRecordingNavigator {

    // DIFFERENCE FROM VANILLA:
    // In Vanilla, we hoisted a `NavHostController`.
    // In Nav3, there is no monolithic NavController. State is explicitly managed by a custom
    // `NavigationState` object which maps NavKeys to SnapshotStateLists. We hoist our wrapper class
    // (`BackStackNavigator`) so both SetupNavController and CreateNavHostForBottom can share the same memory reference.
    private val backStackNavigatorState: MutableState<BackStackNavigator?> = mutableStateOf(null)

    init {
        Log.d("LogBackStack - SupremeNavigatorNav3", "init: ${this.backStack}")
    }

    override fun onTabClick(tab: TabState) {
        onTabClickNav3(
            isSelected = tab.isSame,
            item = tab.current,
            backStackNavigator = backStackNavigatorState.value!!,
        )
    }

    internal fun onTabClickNav3(
        isSelected: Boolean,
        item: BottomNavigationItem,
        backStackNavigator: BackStackNavigator,
    ) {
        logTabClickNav3(item, backStackNavigator.state)

        // DIFFERENCE FROM VANILLA:
        // Vanilla uses `navController.popBackStack(start, false)` to clear a tab.
        // Nav3 relies on our custom `popToRoot()` extension which manually removes elements
        // from the `SnapshotStateList` of the currently active tab until only the root remains.
        if (isSelected) {
            Log.d("LogBackStack", "onTabClickNav3: Selected same tab ($item). Popping to root.")
            backStackNavigator.popToRoot()
            return
        }

        val direction = getRouteOnTabClickNav3(item)
        Log.d("LogBackStack", "onTabClickNav3: Navigating to $item")

        // DIFFERENCE FROM VANILLA:
        // Vanilla uses `popUpTo(rootGraph) { saveState = true }` and `restoreState = true`
        // to swap tabs while preserving backstacks.
        // Nav3 achieves this implicitly by just changing the `topLevelRoute` in `NavigationState`.
        // The stacks are naturally preserved in `NavigationState.backStacks` Map.
        backStackNavigator.navigate(direction)
    }

    private fun getRouteOnTabClickNav3(route: BottomNavigationItem): NavKey = when(route) {
        BottomNavigationItem.Camera -> GraphNav3.BottomTab.CameraRecording.CameraRecordingDestinationNav3
        BottomNavigationItem.Demo -> GraphNav3.BottomTab.Demo.DemoDestinationNav3
        BottomNavigationItem.Setting -> GraphNav3.BottomTab.SettingDestinationNav3
    }

    @Composable
    override fun CreateNavHostForBottom(
        modifier: Modifier,
        startRoute: BottomNavigationItem,
    ) {
        val backStackNavigator = backStackNavigatorState.value ?: return
        val navigationState = backStackNavigator.state

        val navigator = remember {
            BottomNavigatorNav3(
                parent = this,
                backStackNavigator = backStackNavigator
            )
        }

        val entryProvider = entryProvider {
            setupBottomNavigationGraphNav3(navigator)
        }

        NavDisplay(
            entries = navigationState.toEntries(entryProvider),
            onBack = { backStackNavigator.goBack() },
            modifier = modifier
        )
    }

    @Composable
    override fun SetupNavController(
        updateBackHandling: (startDestinationRoster: List<String?>, currentRoute: String?) -> Unit,
        onTabChanged: (BottomNavigationItem) -> Unit,
    ) {
        val navigationState = rememberNavigationState(
            startRoute = GraphNav3.BottomTab.Demo.DemoDestinationNav3,
            topLevelRoutes = BOTTOM_TAB_TOP_LEVEL_ROUTES
        )

        val navigator = remember(navigationState) { BackStackNavigator(navigationState) }

        if (backStackNavigatorState.value != navigator) {
            backStackNavigatorState.value = navigator
        }

        LogNavigationEventsNav3(navigationState = navigationState, label = "BottomTab")

        val currentTabRoute = navigationState.topLevelRoute
        val activeStack = navigationState.backStacks[currentTabRoute]
        val currentDestination = activeStack?.lastOrNull()
        // We also track stackSize so the LaunchedEffect triggers even if we pop/push the same exact route type.
        val stackSize = activeStack?.size ?: 0

        // DIFFERENCE FROM VANILLA:
        // Vanilla collects the `navController.currentBackStackEntryFlow`.
        // Nav3 triggers a standard Compose LaunchedEffect whenever the MutableState (`topLevelRoute`)
        // or the SnapshotStateList (`activeStack`) changes.
        LaunchedEffect(currentTabRoute, currentDestination, stackSize) {
            if (currentDestination == null) return@LaunchedEffect

            val startDestinations = BOTTOM_TAB_TOP_LEVEL_ROUTES.map { it.toString() }
            updateBackHandling(startDestinations, currentDestination.toString())

            // Sync current tab state with nav controller destination
            val currentTab = when (currentTabRoute) {
                GraphNav3.BottomTab.CameraRecording.CameraRecordingDestinationNav3 -> BottomNavigationItem.Camera
                GraphNav3.BottomTab.SettingDestinationNav3 -> BottomNavigationItem.Setting
                else -> BottomNavigationItem.Demo
            }
            onTabChanged(currentTab)
        }
    }

    override fun goToSettingFromCameraRecording() {
        backStack.add(GraphNav3.Main.SettingDestinationNav3)
    }
}

private fun logTabClickNav3(tab: BottomNavigationItem, state: NavigationState) {
    val currentTopLevel = state.topLevelRoute
    val currentStack = state.backStacks[currentTopLevel]
    val currentDest = currentStack?.lastOrNull() ?: currentTopLevel

    Log.d("LogBackStack", """
            --- ON TAB CLICK ($tab) ---
            Current Top Level Tab: $currentTopLevel
            Current Dest Location: $currentDest
            Stack Size for Tab: ${currentStack?.size ?: 0}
            -------------------------------------
        """.trimIndent())
}

@Composable
private fun LogNavigationEventsNav3(navigationState: NavigationState, label: String) {
    val currentTabRoute = navigationState.topLevelRoute
    val activeStack = navigationState.backStacks[currentTabRoute]
    val currentDestination = activeStack?.lastOrNull()
    val stackSize = activeStack?.size ?: 0

    // Re-run logging whenever the stack structure changes
    LaunchedEffect(currentTabRoute, currentDestination, stackSize) {
        val sb = StringBuilder()
        sb.append("\n--- NAV STATE CHANGE ($label) ---\n")
        sb.append("Active Top-Level Tab: $currentTabRoute\n")
        sb.append("Current Dest: ${currentDestination ?: currentTabRoute}\n")
        sb.append("Stack Size: $stackSize\n")

        // Print out the entire backstack for the active tab
        activeStack?.forEachIndexed { index, navKey ->
            sb.append("  [$index] $navKey \n")
            // Nav3 doesn't have standard Lifecycle states attached to entries quite like Vanilla,
            // so we omit the (Lifecycle: STARTED) log here, focusing on the Route keys.
        }
        sb.append("--------------------------------\n")
        Log.d("LogBackStack", sb.toString())
    }
}
