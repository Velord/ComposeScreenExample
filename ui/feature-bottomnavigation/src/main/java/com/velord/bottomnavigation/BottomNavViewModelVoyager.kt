package com.velord.bottomnavigation

import cafe.adriel.voyager.core.screen.Screen
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class BottomNavViewModelVoyager : CoroutineScopeViewModel() {

    val currentTabFlow = MutableStateFlow<BottomNavigationTab>(BottomNavigationTab.Camera)
    val isBackHandlingEnabledFlow = MutableStateFlow(true)
    val finishAppEvent = MutableStateFlow(false)

    fun getNavigationItems(): List<BottomNavigationTab> = BottomNavigationTab::class
        .sealedSubclasses
        .map { it.objectInstance!! }

    fun onTabClick(newTab: BottomNavigationTab) {
        if (newTab == currentTabFlow.value) return
        currentTabFlow.value = newTab
    }

    fun onBackDoubleClick() = launch {
        finishAppEvent.emit(true)
    }

    fun updateBackHandling(currentNavigationDestination: Screen?) {
        val isStart = currentNavigationDestination == BottomNavScreen
        isBackHandlingEnabledFlow.value = isStart
    }
}