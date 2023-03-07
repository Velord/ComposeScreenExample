package com.velord.composescreenexample.ui.main.bottomNav

import com.velord.composescreenexample.utils.navigation.BottomNavigationItem
import com.velord.composescreenexample.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomNavViewModel @Inject constructor(
) : BaseViewModel() {

    val tabFlow: MutableStateFlow<BottomNavigationItem> = MutableStateFlow(BottomNavigationItem.Camera)
    val finishAppEvent: MutableSharedFlow<Unit> = MutableSharedFlow()

    fun onTabClick(newTab: BottomNavigationItem) {
        if (newTab == tabFlow.value) return
        tabFlow.value = newTab
    }

    fun onBackDoubleClick() = launch {
        finishAppEvent.emit(Unit)
    }
}