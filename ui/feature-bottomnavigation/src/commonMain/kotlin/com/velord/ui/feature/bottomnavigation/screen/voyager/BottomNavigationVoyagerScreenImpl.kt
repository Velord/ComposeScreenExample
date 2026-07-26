@file:JvmName("BottomNavigationVoyagerScreenKt")

package com.velord.ui.feature.bottomnavigation.screen.voyager

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.core.ui.util.ObserveSharedFlow
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem
import com.velord.ui.feature.bottomnavigation.screen.component.ScreenSetup
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationVM
import com.velord.ui.feature.bottomnavigation.screen.component.Content as BottomNavigationContent

private fun createBottomNavigationTab(
    item: BottomNavigationItem
): VoyagerBottomNavigationTab = when (item) {
    BottomNavigationItem.Camera -> VoyagerBottomNavigationTab.Camera
    BottomNavigationItem.Demo -> VoyagerBottomNavigationTab.Demo
    BottomNavigationItem.Setting -> VoyagerBottomNavigationTab.Settings
}

@Composable
fun BottomNavigationVoyagerScreenImpl(viewModel: BottomNavigationVM) {
    val uiState = viewModel.uiStateFlow.collectAsStateWithLifecycle()
    /*
    The LocalNavigator available here is the outer app navigator that hosts this bottom-navigation
    screen. Each tab creates a separate nested Navigator for its own back stack. The nested tab
    reports its navigator and neutral route identities through LocalVoyagerNavigatorObserver because
    a CompositionLocal cannot expose a child value to its parent. Navigator remains inside this
    Voyager adapter; only route identities enter the VM.
    */
    val activeNavigatorState = remember { mutableStateOf<Navigator?>(null) }

    ScreenSetup(
        state = uiState,
        backBehavior = uiState.value.backBehavior,
        onBackClick = {
            viewModel.onAction(BottomNavigationUiAction.BackClick)
        },
        onBackDoubleClick = {
            viewModel.onAction(BottomNavigationUiAction.BackDoubleClick)
        },
        libSetup = {
            // What if we want to not block system back and allow just exit the app ?
            // In that case, we can simply not call graphCompletedHandling() at all,
            // and the back handling will not be enabled,
            // allowing the system to handle the back press as usual.
            // LocalNavigator here resolves the outer navigator, not the active tab stack.
        },
    ) {
        ObserveSharedFlow(flow = viewModel.onTabClickEvent) { tabState ->
            if (tabState.isSame) activeNavigatorState.value?.popAll()
        }

        Content(
            currentItem = uiState.value.tabState.current,
            getNavigationItemRoster = viewModel::getNavigationItemRoster,
            onTabClick = { tab ->
                viewModel.onAction(BottomNavigationUiAction.TabClick(tab))
            },
            // libSetup sees the outer navigator; this callback observes the active nested stack.
            onNavigatorChanged = { navigator, startDestinationRoster, currentRoute ->
                activeNavigatorState.value = navigator
                val action = BottomNavigationUiAction.UpdateBackHandling(
                    startDestinationRoster = startDestinationRoster,
                    currentRoute = currentRoute,
                )
                viewModel.onAction(action)
            },
        )
    }
}

@Composable
private fun Content(
    currentItem: BottomNavigationItem,
    getNavigationItemRoster: () -> List<BottomNavigationItem>,
    onTabClick: (BottomNavigationItem) -> Unit,
    onNavigatorChanged: (Navigator, List<String?>, String?) -> Unit,
) {
    val tab = createBottomNavigationTab(currentItem)
    TabNavigator(tab) {
        val tabNavigator = LocalTabNavigator.current
        LaunchedEffect(tab) {
            tabNavigator.current = tab
        }

        BottomNavigationContent(
            selectedItem = currentItem,
            navigationItemRoster = getNavigationItemRoster(),
            onClick = onTabClick,
            content = { padding ->
                Surface(Modifier.padding(bottom = padding.calculateBottomPadding())) {
                    CompositionLocalProvider(
                        LocalVoyagerNavigatorObserver provides onNavigatorChanged,
                    ) {
                        CurrentTab()
                    }
                }
            },
        )
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    BottomNavigationContent(
        selectedItem = BottomNavigationItem.Camera,
        navigationItemRoster = BottomNavigationItem.entries,
        onClick = {},
        content = {},
    )
}
