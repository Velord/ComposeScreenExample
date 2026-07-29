package com.velord.ui.feature.bottomnavigation.viewmodel

import com.velord.ui.feature.bottomnavigation.navigation.BottomNavBackHandlingState
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavEventService
import com.velord.ui.feature.bottomnavigation.navigation.TabState

data class BottomNavigationUiState(
    val tabState: TabState,
    val backHandlingState: BottomNavBackHandlingState,
    val isConfirmExitRequested: Boolean,
) {
    val backBehavior: BottomNavigationBackBehavior get() {
        val isNotStart = backHandlingState.isAtStartGraphDestination.not()
        return when {
            isNotStart -> BottomNavigationBackBehavior.DelegateToNavigator
            tabState.current.isCamera -> BottomNavigationBackBehavior.ReturnToDefaultTab
            (tabState.current.isDemo || tabState.current.isSetting) && backHandlingState.isEnabled ->
                BottomNavigationBackBehavior.ConfirmExit
            else -> BottomNavigationBackBehavior.DelegateToNavigator
        }
    }

    companion object {
        fun from(service: BottomNavEventService) = BottomNavigationUiState(
            tabState = service.currentTabStateFlow.value,
            backHandlingState = service.backHandlingStateFlow.value,
            isConfirmExitRequested = service.confirmExitRequestedFlow.value,
        )
    }
}
