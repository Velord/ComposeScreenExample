package com.velord.bottomnavigation

import androidx.navigation.NavDestination
import com.velord.multiplebackstackapplier.utils.isCurrentStartDestination
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class BottomNavViewModelJetpack(
    private val bottomNavEventService: BottomNavEventService
): CoroutineScopeViewModel() {

    val currentTabFlow = MutableStateFlow(BottomNavigationItem.Camera)
    val backHandlingStateFlow = bottomNavEventService.backHandlingStateFlow
    val finishAppEvent: MutableSharedFlow<Unit> = MutableSharedFlow()

    fun getNavigationItems() = BottomNavigationItem.entries

    fun onTabClick(newTab: BottomNavigationItem) {
        if (newTab == currentTabFlow.value) return
        currentTabFlow.value = newTab
    }

    fun onBackDoubleClick() = launch {
        finishAppEvent.emit(Unit)
    }

    fun updateBackHandling(currentNavigationDestination: NavDestination?) {
        val isStart = currentNavigationDestination.isCurrentStartDestination(getNavigationItems())
        val newState = backHandlingStateFlow.value.copy(isAtStartGraphDestination = isStart)
        bottomNavEventService.updateBackHandlingState(newState)
    }

    private fun changeGrantedToProceed(isGranted: Boolean) {
        val newState = backHandlingStateFlow.value.copy(isGrantedToProceed = isGranted)
        bottomNavEventService.updateBackHandlingState(newState)
    }

    fun graphCompletedHandling() {
        changeGrantedToProceed(true)
    }

    fun graphTakeResponsibility() {
        changeGrantedToProceed(false)
    }
}