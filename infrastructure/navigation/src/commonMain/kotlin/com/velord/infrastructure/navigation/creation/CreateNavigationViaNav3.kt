package com.velord.infrastructure.navigation.creation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.velord.infrastructure.navigation.compose.nav3.GraphNav3
import com.velord.infrastructure.navigation.compose.nav3.graph.setupMainGraphNav3
import com.velord.infrastructure.navigation.compose.nav3.navigator.SupremeNavigatorNav3

@Composable
fun CreateNavigationViaNav3() {
    val startDestination = GraphNav3.Main.BottomNavigationDestinationNav3
    val backStack = remember { mutableStateListOf<GraphNav3>(startDestination) }
    val navigator = SupremeNavigatorNav3(backStack)

    /*
    Every navigation implementation follows the same hierarchy: the app host opens the bottom
    navigation host, then the bottom host opens the graph owned by the selected tab.

    Nav3 models both levels explicitly. This outer backStack starts with BottomNavigationScreen
    and receives app-level destinations such as Setting. BottomNavigationScreen delegates its
    nested host to SupremeNavigatorNav3, where NavigationState keeps one saved back stack for each
    top-level tab. Selecting another tab switches the active stack; selecting the current tab pops
    only that tab to its root. NavDisplay.onBack removes only the last outer destination. Back
    behavior inside a selected tab remains owned by the nested host. Keep this start destination,
    the NavigationState start route, and TabState.DEFAULT aligned when changing the initial screen.
    */

    NavDisplay(
        backStack = backStack, // Custom-managed back stack
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            setupMainGraphNav3(navigator)
        }
    )
}
