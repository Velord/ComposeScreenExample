package com.velord.ui.feature.bottomnavigation.viewmodel

import com.velord.ui.feature.bottomnavigation.navigation.BottomNavBackHandlingState
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem
import com.velord.ui.feature.bottomnavigation.navigation.TabState

data class BottomNavigationUiState(
    val tabState: TabState,
    val backHandlingState: BottomNavBackHandlingState,
) {
    val backBehavior: BottomNavigationBackBehavior get() = when {
        backHandlingState.isAtStartGraphDestination.not() ->
            BottomNavigationBackBehavior.DelegateToNavigator
        tabState.current == BottomNavigationItem.Camera ->
            BottomNavigationBackBehavior.ReturnToDefaultTab
        tabState.current == BottomNavigationItem.Setting && backHandlingState.isEnabled ->
            BottomNavigationBackBehavior.ConfirmExit
        else -> BottomNavigationBackBehavior.DelegateToNavigator
    }
}
