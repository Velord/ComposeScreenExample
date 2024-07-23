package com.velord.bottomnavigation.viewmodel

import androidx.navigation.NavDestination
import com.velord.bottomnavigation.BottomNavEventService
import com.velord.bottomnavigation.screen.BottomNavigationDestination
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

data class TabState(
    val previous: BottomNavigationDestination,
    val current: BottomNavigationDestination
) {
    val isSame: Boolean get() = previous == current

    companion object {
        val Default = TabState(
            previous = BottomNavigationDestination.Demo,
            current = BottomNavigationDestination.Demo
        )
    }
}

@KoinViewModel
class BottomNavigationDestinationsVM(
    private val bottomNavEventService: BottomNavEventService
): CoroutineScopeViewModel() {

    val currentTabFlow = MutableStateFlow(TabState.Default)
    val backHandlingStateFlow = bottomNavEventService.backHandlingStateFlow
    val finishAppEvent = MutableSharedFlow<Unit>()

    fun onTabClick(newTab: BottomNavigationDestination) {
        currentTabFlow.update {
            it.copy(
                previous = it.current,
                current = newTab
            )
        }
    }

    fun onBackDoubleClick() = launch {
        finishAppEvent.emit(Unit)
    }

    fun getNavigationItems() = BottomNavigationDestination.entries

    fun updateBackHandling(
        startDestinationRoster: List<String?>,
        dest: NavDestination?
    ) {
        val isStart = startDestinationRoster.contains(dest?.route)
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