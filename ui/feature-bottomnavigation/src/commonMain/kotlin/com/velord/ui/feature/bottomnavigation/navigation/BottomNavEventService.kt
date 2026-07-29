package com.velord.ui.feature.bottomnavigation.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

data class BottomNavBackHandlingState(
    val isAtStartGraphDestination: Boolean = true,
    val isGrantedToProceed: Boolean = false
) {
    val isEnabled: Boolean = isAtStartGraphDestination && isGrantedToProceed

    companion object {
        val DEFAULT = BottomNavBackHandlingState()
    }
}

class BottomNavEventService {

    val backHandlingStateFlow = MutableStateFlow(BottomNavBackHandlingState())
    val currentTabStateFlow = MutableStateFlow(TabState.DEFAULT)
    val confirmExitRequestedFlow = MutableStateFlow(false)
    val onTabClickEvent = MutableSharedFlow<TabState>()

    fun updateBackHandlingState(newState: BottomNavBackHandlingState) {
        backHandlingStateFlow.value =  newState
    }

    fun updateTab(newTab: TabState) {
        currentTabStateFlow.value = newTab
    }

    fun updateConfirmExitRequested(isRequested: Boolean) {
        confirmExitRequestedFlow.value = isRequested
    }

    suspend fun emitTabClick(tab: TabState) {
        onTabClickEvent.emit(tab)
    }
}

