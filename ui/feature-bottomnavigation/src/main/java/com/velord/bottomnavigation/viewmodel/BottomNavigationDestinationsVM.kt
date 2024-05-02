package com.velord.bottomnavigation.viewmodel

import androidx.navigation.NavDestination
import com.velord.bottomnavigation.BottomNavEventService
import com.velord.bottomnavigation.screen.BottomNavigationDestination
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class BottomNavigationDestinationsVM(
    private val bottomNavEventService: BottomNavEventService
): CoroutineScopeViewModel() {

    val currentTabFlow = MutableStateFlow(BottomNavigationDestination.Demo)
    val backHandlingStateFlow = bottomNavEventService.backHandlingStateFlow
    val finishAppEvent: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun onTabClick(newTab: BottomNavigationDestination) {
        if (newTab == currentTabFlow.value) return
        currentTabFlow.value = newTab
    }

    fun onBackDoubleClick() = launch {
        finishAppEvent.emit(true)
    }

    fun getNavigationItems() = BottomNavigationDestination.entries

    fun updateBackHandling(dest: NavDestination?) {
       // val isStart = currentNavigationDestination.isCurrentStartDestination(graphBackHandlerToTab)
        val newState = backHandlingStateFlow.value.copy(isAtStartGraphDestination = false)
        bottomNavEventService.updateBackHandlingState(newState)
    }
}