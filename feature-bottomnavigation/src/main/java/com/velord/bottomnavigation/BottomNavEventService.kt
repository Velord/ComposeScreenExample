package com.velord.bottomnavigation

import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.annotation.Single

data class BottomNavBackHandlingState(
    val isAtStartGraphDestination: Boolean = true,
    val isGrantedToProceed: Boolean = false
) {
    val isEnabled: Boolean get() = isAtStartGraphDestination && isGrantedToProceed
}

interface BottomNavEventServiceStub

@Single
class BottomNavEventService : BottomNavEventServiceStub {

    val backHandlingStateFlow = MutableStateFlow(BottomNavBackHandlingState())
    val currentTabFlow = MutableStateFlow(BottomNavigationItem.Camera)

    fun updateBackHandlingState(newState: BottomNavBackHandlingState) {
        backHandlingStateFlow.value =  newState
    }

    fun updateTab(newTab: BottomNavigationItem) {
        if (newTab == currentTabFlow.value) return
        currentTabFlow.value = newTab
    }
}