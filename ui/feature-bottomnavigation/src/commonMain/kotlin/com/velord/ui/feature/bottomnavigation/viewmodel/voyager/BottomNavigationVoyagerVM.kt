package com.velord.ui.feature.bottomnavigation.viewmodel.voyager

import cafe.adriel.voyager.core.screen.Screen
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem
import com.velord.ui.sharedviewmodel.CoroutineScopeVM
import com.velord.usecase.event.RequestAppExitUC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal data class VoyagerBackHandling(
    val isExitEnabled: Boolean,
    val isTabReturnEnabled: Boolean,
)

class BottomNavigationVoyagerVM(
    private val requestAppExitUC: RequestAppExitUC,
) : CoroutineScopeVM() {

    val uiStateFlow = MutableStateFlow(BottomNavigationVoyagerUiState.DEFAULT)
    private val actionFlow = MutableSharedFlow<BottomNavigationVoyagerUiAction>()

    init {
        observe()
    }

    fun getNavigationItemRoster(): List<BottomNavigationItem> = BottomNavigationItem.entries

    fun onAction(action: BottomNavigationVoyagerUiAction) {
        launch {
            actionFlow.emit(action)
        }
    }

    private fun onTabClick(newTab: BottomNavigationItem) {
        if (newTab == uiStateFlow.value.currentTab) return

        uiStateFlow.update { state ->
            state.copy(
                currentTab = newTab,
                isBackHandlingEnabled = false,
                isTabBackHandlingEnabled = false,
            )
        }
    }

    private fun onBackDoubleClick() = launch { requestAppExitUC() }

    private fun onBackClick() {
        onTabClick(BottomNavigationVoyagerUiState.DEFAULT.currentTab)
    }

    private fun onUpdateBackHandling(startDestination: Screen, currentDestination: Screen) {
        val backHandling = resolveVoyagerBackHandling(
            currentTab = uiStateFlow.value.currentTab,
            startDestination = startDestination,
            currentDestination = currentDestination,
        )
        uiStateFlow.update { state ->
            state.copy(
                isBackHandlingEnabled = backHandling.isExitEnabled,
                isTabBackHandlingEnabled = backHandling.isTabReturnEnabled,
            )
        }
    }

    internal fun resolveVoyagerBackHandling(
        currentTab: BottomNavigationItem,
        startDestination: Screen,
        currentDestination: Screen,
    ): VoyagerBackHandling {
        val isAtTabRoot = currentDestination == startDestination
        val isAtDefaultTab = currentTab == BottomNavigationVoyagerUiState.DEFAULT.currentTab

        return VoyagerBackHandling(
            isExitEnabled = isAtTabRoot && isAtDefaultTab,
            isTabReturnEnabled = isAtTabRoot && isAtDefaultTab.not(),
        )
    }

    private fun observe() {
        launch {
            actionFlow.collect { action ->
                when (action) {
                    is BottomNavigationVoyagerUiAction.TabClick -> onTabClick(action.newTab)
                    is BottomNavigationVoyagerUiAction.BackDoubleClick -> onBackDoubleClick()
                    is BottomNavigationVoyagerUiAction.BackClick -> onBackClick()
                    is BottomNavigationVoyagerUiAction.UpdateBackHandling -> onUpdateBackHandling(
                        startDestination = action.startDestination,
                        currentDestination = action.currentDestination,
                    )
                }
            }
        }
    }
}
