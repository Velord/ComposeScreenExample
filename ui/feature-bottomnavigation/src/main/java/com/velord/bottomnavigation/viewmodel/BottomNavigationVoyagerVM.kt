package com.velord.bottomnavigation.viewmodel

import cafe.adriel.voyager.core.screen.Screen
import com.velord.bottomnavigation.screen.voyager.BottomNavigationTab
import com.velord.bottomnavigation.screen.voyager.BottomNavigationVoyagerScreen
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class BottomNavigationVoyagerVM : CoroutineScopeViewModel() {

    val currentTabFlow = MutableStateFlow<BottomNavigationTab>(BottomNavigationTab.Camera)
    val isBackHandlingEnabledFlow = MutableStateFlow(true)
    val finishAppEvent: MutableSharedFlow<Boolean> = MutableSharedFlow()

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
        val isStart = currentNavigationDestination == BottomNavigationVoyagerScreen
        isBackHandlingEnabledFlow.value = isStart
    }
}