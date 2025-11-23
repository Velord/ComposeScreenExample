package com.velord.bottomnavigation.viewmodel

import com.velord.bottomnavigation.BottomNavEventService
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
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

    val currentTabFlow = MutableSharedFlow<TabState>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val backHandlingStateFlow = bottomNavEventService.backHandlingStateFlow
    val finishAppEvent = MutableSharedFlow<Unit>()

    init {
        launch {
            currentTabFlow.emit(TabState.Default)
        }
    }

    fun onTabClick(newTab: BottomNavigationItem) {
        launch {
            val current = currentTabFlow.take(1).first()
            val new = current.copy(previous = current.current, current = newTab)
            currentTabFlow.emit(new)
        }
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