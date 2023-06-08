package com.velord.bottomnavigation

import androidx.navigation.NavDestination
import com.velord.multiplebackstackapplier.utils.isCurrentStartDestination
import com.velord.util.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomNavViewModel @Inject constructor() : BaseViewModel() {

    val currentTabFlow = MutableStateFlow(BottomNavigationItem.Camera)
    val isBackHandlingEnabledFlow = MutableStateFlow(false)
    val finishAppEvent: MutableSharedFlow<Unit> = MutableSharedFlow()

    fun getNavigationItems() = BottomNavigationItem.values().toList()

    fun onTabClick(newTab: BottomNavigationItem) {
        if (newTab == currentTabFlow.value) return
        currentTabFlow.value = newTab
    }

    fun onBackDoubleClick() = launch {
        finishAppEvent.emit(Unit)
    }

    fun updateBackHandling(currentNavigationDestination: NavDestination?) {
        val isStart = currentNavigationDestination.isCurrentStartDestination(getNavigationItems())
        isBackHandlingEnabledFlow.value = isStart
    }
}