package com.velord.ui.feature.bottomnavigation.navigation

import kotlinx.coroutines.flow.MutableStateFlow

data class BottomNavBackHandlingState(
    val isAtStartGraphDestination: Boolean = true,
    val isGrantedToProceed: Boolean = false
) {
    val isEnabled: Boolean = isAtStartGraphDestination && isGrantedToProceed
}

class BottomNavEventService {

    val backHandlingStateFlow = MutableStateFlow(BottomNavBackHandlingState())
    val currentTabStateFlow = MutableStateFlow(TabState.DEFAULT)

    fun updateBackHandlingState(newState: BottomNavBackHandlingState) {
        backHandlingStateFlow.value =  newState
    }

    fun updateTab(newTab: TabState) {
        currentTabStateFlow.value = newTab
    }
}
