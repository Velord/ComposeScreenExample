package com.velord.bottomnavigation

import cafe.adriel.voyager.core.screen.Screen
import com.example.sharedviewmodel.CoroutineScopeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomNavViewModelVoyager @Inject constructor(): CoroutineScopeViewModel() {

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