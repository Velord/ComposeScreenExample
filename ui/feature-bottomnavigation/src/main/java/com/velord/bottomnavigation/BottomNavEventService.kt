package com.velord.bottomnavigation

import com.velord.bottomnavigation.viewmodel.TabState
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.annotation.Single

data class BottomNavBackHandlingState(
    val isAtStartGraphDestination: Boolean = true,
    val isGrantedToProceed: Boolean = false
) {
    val isEnabled: Boolean get() = isAtStartGraphDestination && isGrantedToProceed
}

@Single
class BottomNavEventService {

    val backHandlingStateFlow = MutableStateFlow(BottomNavBackHandlingState())
    val currentTabStateFlow = MutableStateFlow(TabState.Default)

    fun updateBackHandlingState(newState: BottomNavBackHandlingState) {
        backHandlingStateFlow.value =  newState
    }

    fun updateTab(newTab: TabState) {
        currentTabStateFlow.value = newTab
    }
}