package com.velord.ui.feature.bottomnavigation.viewmodel

import cafe.adriel.voyager.core.screen.Screen
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem
import com.velord.ui.feature.bottomnavigation.screen.voyager.BottomNavigationVoyagerScreen
import com.velord.ui.sharedviewmodel.CoroutineScopeVM
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class BottomNavigationVoyagerVM : CoroutineScopeVM() {

    val uiStateFlow = MutableStateFlow(
        BottomNavigationVoyagerUiState(
            currentTab = BottomNavigationItem.Camera,
            isBackHandlingEnabled = true,
        )
    )
    val finishAppEvent: MutableSharedFlow<Boolean> = MutableSharedFlow()
    private val actionFlow = MutableSharedFlow<BottomNavigationVoyagerUiAction>()

    init {
        observe()
    }

    fun getNavigationItems(): List<BottomNavigationItem> = BottomNavigationItem.entries

    fun onAction(action: BottomNavigationVoyagerUiAction) {
        launch {
            actionFlow.emit(action)
        }
    }

    private fun onTabClick(newTab: BottomNavigationItem) {
        if (newTab == uiStateFlow.value.currentTab) return
        uiStateFlow.update { state -> state.copy(currentTab = newTab) }
    }

    private fun onBackDoubleClick() = launch {
        finishAppEvent.emit(true)
    }

    private fun onUpdateBackHandling(currentNavigationDestination: Screen?) {
        val isStart = currentNavigationDestination == BottomNavigationVoyagerScreen
        uiStateFlow.update { state -> state.copy(isBackHandlingEnabled = isStart) }
    }

    private fun observe() {
        launch {
            actionFlow.collect { action ->
                when (action) {
                    is BottomNavigationVoyagerUiAction.TabClick -> onTabClick(action.newTab)
                    is BottomNavigationVoyagerUiAction.BackDoubleClick -> onBackDoubleClick()
                    is BottomNavigationVoyagerUiAction.UpdateBackHandling ->
                        onUpdateBackHandling(action.currentNavigationDestination)
                }
            }
        }
    }
}
