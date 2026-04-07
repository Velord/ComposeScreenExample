package com.velord.bottomnavigation.viewmodel

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
            onTabClickEvent.emit(newState)
        }
    }

    // (Back Press -> Updates State ONLY)
    fun onTabDestinationChanged(newTab: BottomNavigationItem) {
        // Previous solution was to reset all time we enter a new tab,
        // But this causes sync issues at a startup time.
        // The "bottom navigation" screen and the "first destination" screen
        // in the graph are into race condition.
        // For example, if the first destination screen is faster to emit the tab state than
        // the bottom navigation screen, then the bottom navigation screen will override
        // it with the default tab state, causing the app to always
        // start with isGrantedToProceed = false
        //graphTakeResponsibility()

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
        val newState = backHandlingStateFlow.value.copy(
            isAtStartGraphDestination = isStart,
            // Auto-revoke the grant if we navigate deep into the stack.
            // If we are at the start, keep whatever the screen requested.
            isGrantedToProceed = if (isStart) {
                backHandlingStateFlow.value.isGrantedToProceed
            } else {
                false
            }
        )
        bottomNavEventService.updateBackHandlingState(newState)
    }

    fun graphCompletedHandling() {
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