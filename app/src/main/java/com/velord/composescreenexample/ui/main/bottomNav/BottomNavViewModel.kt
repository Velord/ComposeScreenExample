package com.velord.composescreenexample.ui.main.bottomNav

import androidx.navigation.NavDestination
import com.velord.composescreenexample.utils.navigation.BottomNavigationItem
import com.velord.composescreenexample.viewModel.BaseViewModel
import com.velord.multiplebackstackapplier.utils.isCurrentStartDestination
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