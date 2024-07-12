package com.velord.bottomnavigation.viewmodel

import androidx.navigation.NavDestination
import com.velord.bottomnavigation.BottomNavEventService
import com.velord.bottomnavigation.BottomNavigationItem
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class BottomNavigationJetpackVM(
    private val bottomNavEventService: BottomNavEventService
): CoroutineScopeViewModel() {

    val currentTabFlow = bottomNavEventService.currentTabFlow
    val backHandlingStateFlow = bottomNavEventService.backHandlingStateFlow
    val finishAppEvent: MutableSharedFlow<Unit> = MutableSharedFlow()

    private val graphBackHandlerToTab = listOf(
        com.velord.resource.R.id.settingsFragment to BottomNavigationItem.Settings,
        com.velord.resource.R.id.demoFragment to BottomNavigationItem.Demo,
        com.velord.resource.R.id.cameraRecordingFragment to BottomNavigationItem.Camera,
    )

    fun getNavigationItems() = BottomNavigationItem.entries

    fun onTabClick(newTab: BottomNavigationItem) {
        bottomNavEventService.updateTab(newTab)
    }

    fun onBackDoubleClick() = launch {
        finishAppEvent.emit(Unit)
    }

    private fun NavDestination?.isCurrentStartDestination(
        items: List<Pair<Int, BottomNavigationItem>>,
    ): Boolean = items.firstOrNull { it.first == this?.id }?.second == currentTabFlow.value

    fun updateBackHandling(currentNavigationDestination: NavDestination?) {
        val isStart = currentNavigationDestination.isCurrentStartDestination(graphBackHandlerToTab)
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