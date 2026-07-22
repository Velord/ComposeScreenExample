@file:JvmName("BottomNavigationVoyagerScreenKt")

package com.velord.ui.feature.bottomnavigation.screen.voyager

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem
import com.velord.ui.feature.bottomnavigation.screen.component.PlatformSingleBackHandler
import com.velord.ui.feature.bottomnavigation.screen.component.ScreenSetup
import com.velord.ui.feature.bottomnavigation.viewmodel.voyager.BottomNavigationVoyagerUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.voyager.BottomNavigationVoyagerVM
import com.velord.ui.feature.bottomnavigation.screen.component.Content as BottomNavigationContent

private fun createBottomNavigationTab(
    item: BottomNavigationItem
): BottomNavigationTab = when (item) {
    BottomNavigationItem.Camera -> BottomNavigationTab.Camera
    BottomNavigationItem.Demo -> BottomNavigationTab.Demo
    BottomNavigationItem.Setting -> BottomNavigationTab.Settings
}

@Composable
fun BottomNavigationVoyagerScreenImpl(viewModel: BottomNavigationVoyagerVM) {
    val uiState = viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val isEnabledState = remember {
        derivedStateOf { uiState.value.isBackHandlingEnabled }
    }
    // TODO: why not use local ?
    val activeNavigatorState = remember { mutableStateOf<Navigator?>(null) }

    ScreenSetup(
        state = uiState,
        isBackHandlingEnabled = isEnabledState.value,
        onBackDoubleClick = {
            viewModel.onAction(BottomNavigationVoyagerUiAction.BackDoubleClick)
        },
        libSetup = {
            // What if we want to not block system back and allow just exit the app ?
            // In that case, we can simply not call graphCompletedHandling() at all,
            // and the back handling will not be enabled,
            // allowing the system to handle the back press as usual.

            // TODO: why it was commented/deleted ?
//            val navigator = LocalNavigator.current
//            val lastItem = navigator?.lastItemOrNull
//            LaunchedEffect(lastItem) {
//                viewModel.onAction(BottomNavigationVoyagerUiAction.UpdateBackHandling(lastItem))
//            }
        },
    ) {
        Content(
            currentItem = uiState.value.currentTab,
            getNavigationItemRoster = viewModel::getNavigationItemRoster,
            onTabClick = { tab ->
                // TODO: this is not UI logic. Why it was changed ?
                if (tab == uiState.value.currentTab) {
                    activeNavigatorState.value?.popAll()
                } else {
                    viewModel.onAction(BottomNavigationVoyagerUiAction.TabClick(tab))
                }
            },
            // TODO: why is it needed if we had libSetup for that ?
            onNavigatorChanged = { navigator, startDestination ->
                activeNavigatorState.value = navigator
                viewModel.onAction(
                    BottomNavigationVoyagerUiAction.UpdateBackHandling(
                        startDestination = startDestination,
                        currentDestination = navigator.lastItem,
                    )
                )
            },
        )
    }

    // TODO: ScreenSetup already has back handler. Why is it needed ?
    PlatformSingleBackHandler(
        isEnabled = uiState.value.isTabBackHandlingEnabled,
        onBackClick = {
            viewModel.onAction(BottomNavigationVoyagerUiAction.BackClick)
        },
    )
}

@Composable
private fun Content(
    currentItem: BottomNavigationItem,
    getNavigationItemRoster: () -> List<BottomNavigationItem>,
    onTabClick: (BottomNavigationItem) -> Unit,
    onNavigatorChanged: (Navigator, Screen) -> Unit,
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
