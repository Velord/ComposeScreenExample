package com.velord.bottomnavigation.viewmodel

import android.util.Log
import com.velord.bottomnavigation.BottomNavEventService
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

enum class BottomNavigationItem {
    Camera,
    Demo,
    Setting;
}

data class TabState(
    val previous: BottomNavigationItem,
    val current: BottomNavigationItem
) {
    val isSame: Boolean get() = previous == current

    companion object {
        val Default = TabState(
            previous = BottomNavigationItem.Demo,
            current = BottomNavigationItem.Demo
        )
    }
}

@KoinViewModel
class BottomNavigationDestinationsVM(
    private val bottomNavEventService: BottomNavEventService
): CoroutineScopeViewModel() {

    val currentTabStateFlow = bottomNavEventService.currentTabStateFlow
    val backHandlingStateFlow = bottomNavEventService.backHandlingStateFlow
    val finishAppEvent = MutableSharedFlow<Unit>()
    val onTabClickEvent = MutableSharedFlow<TabState>()

    fun onTabClick(newTab: BottomNavigationItem) {
        launch {
            val newState = updateTabStateInternal(newTab)
            Log.d("@@@", "onTabClick: $newState")
            onTabClickEvent.emit(newState)
        }
    }

    // (Back Press -> Updates State ONLY)
    fun onTabDestinationChanged(newTab: BottomNavigationItem) {
        // We only update the UI state so the bottom bar highlights correctly.
        // We do NOT emit to onTabClickEvent, preventing the navigation loop.
        if (currentTabStateFlow.value.current == newTab) return

        updateTabStateInternal(newTab)
    }

    private fun updateTabStateInternal(newTab: BottomNavigationItem): TabState {
        val current = currentTabStateFlow.value
        val new = current.copy(previous = current.current, current = newTab)
        bottomNavEventService.updateTab(new)
        return new
    }

    fun onBackDoubleClick() = launch {
        finishAppEvent.emit(Unit)
    }

    fun getNavigationItems() = BottomNavigationItem.entries

    fun updateBackHandling(
        startDestinationRoster: List<String?>,
        currentRoute: String?
    ) {
        val isStart = startDestinationRoster.contains(currentRoute)
        val newState = backHandlingStateFlow.value.copy(isAtStartGraphDestination = isStart)
        bottomNavEventService.updateBackHandlingState(newState)
        Log.d("@@@", "updateBackHandling: $newState")
    }

    fun graphCompletedHandling() {
        Log.d("@@@", "graphCompletedHandling")
        changeGrantedToProceed(true)
    }

    fun graphTakeResponsibility() {
        changeGrantedToProceed(false)
    }

    private fun changeGrantedToProceed(isGranted: Boolean) {
        val newState = backHandlingStateFlow.value.copy(isGrantedToProceed = isGranted)
        bottomNavEventService.updateBackHandlingState(newState)
    }
}