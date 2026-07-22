package com.velord.ui.feature.bottomnavigation.viewmodel

import com.velord.ui.feature.bottomnavigation.navigation.BottomNavBackHandlingState
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavEventService
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem
import com.velord.ui.feature.bottomnavigation.navigation.TabState
import com.velord.ui.sharedviewmodel.CoroutineScopeVM
import com.velord.usecase.event.RequestAppExitUC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BottomNavigationVM(
    private val bottomNavEventService: BottomNavEventService,
    private val requestAppExitUC: RequestAppExitUC,
) : CoroutineScopeVM() {

    val uiStateFlow = MutableStateFlow(
        BottomNavigationUiState(
            tabState = bottomNavEventService.currentTabStateFlow.value,
            backHandlingState = bottomNavEventService.backHandlingStateFlow.value,
        )
    )
    val onTabClickEvent = MutableSharedFlow<TabState>()
    private val actionFlow = MutableSharedFlow<BottomNavigationUiAction>()

    init {
        observe()
    }

    fun onAction(action: BottomNavigationUiAction) {
        launch {
            actionFlow.emit(action)
        }
    }

    // (Back Press -> Updates State ONLY)
    private fun onTabDestinationChanged(newTab: BottomNavigationItem) {
        // Previous solution was to reset all time we enter a new tab,
        // But this causes sync issues at a startup time.
        // The "bottom navigation" screen and the "first destination" screen
        // in the graph are into race condition.
        // For example, if the first destination screen is faster to emit the tab state than
        // the bottom navigation screen, then the bottom navigation screen will override
        // it with the default tab state, causing the app to always
        // start with isGrantedToProceed = false
        //graphTakeResponsibility()

        // We only update the UI state so the bottom bar highlights correctly.
        // We do NOT emit to onTabClickEvent, preventing the navigation loop.
        if (uiStateFlow.value.tabState.current == newTab) return

        updateTabStateInternal(newTab)
    }

    private fun updateTabStateInternal(newTab: BottomNavigationItem): TabState {
        val current = uiStateFlow.value.tabState
        val new = current.copy(previous = current.current, current = newTab)
        bottomNavEventService.updateTab(new)
        uiStateFlow.update { state -> state.copy(tabState = new) }
        return new
    }

    private fun onTabClick(newTab: BottomNavigationItem) = launch {
        val newState = updateTabStateInternal(newTab)
        onTabClickEvent.emit(newState)
    }

    private fun onBackDoubleClick() = launch {
        requestAppExitUC()
    }

    fun getNavigationItemRoster() = BottomNavigationItem.entries

    private fun onUpdateBackHandling(
        startDestinationRoster: List<String?>,
        currentRoute: String?
    ) {
        val isStart = startDestinationRoster.contains(currentRoute)
        val newState = uiStateFlow.value.backHandlingState.copy(
            isAtStartGraphDestination = isStart,
            // Auto-revoke the grant if we navigate deep into the stack.
            // If we are at the start, keep whatever the screen requested.
            isGrantedToProceed = if (isStart) {
                uiStateFlow.value.backHandlingState.isGrantedToProceed
            } else {
                false
            }
        )
        updateBackHandlingState(newState)
    }

    private fun onGraphCompletedHandling() {
        changeGrantedToProceed(true)
    }

    private fun onGraphTakeResponsibility() {
        changeGrantedToProceed(false)
    }

    private fun changeGrantedToProceed(isGranted: Boolean) {
        val newState = uiStateFlow.value.backHandlingState.copy(isGrantedToProceed = isGranted)
        updateBackHandlingState(newState)
    }

    private fun updateBackHandlingState(newState: BottomNavBackHandlingState) {
        bottomNavEventService.updateBackHandlingState(newState)
        uiStateFlow.update { state -> state.copy(backHandlingState = newState) }
    }

    private fun observe() {
        launch {
            actionFlow.collect { action ->
                when (action) {
                    is BottomNavigationUiAction.TabClick -> onTabClick(action.newTab)
                    is BottomNavigationUiAction.TabDestinationChanged -> onTabDestinationChanged(action.newTab)
                    is BottomNavigationUiAction.BackDoubleClick -> onBackDoubleClick()
                    is BottomNavigationUiAction.UpdateBackHandling -> onUpdateBackHandling(
                        startDestinationRoster = action.startDestinationRoster,
                        currentRoute = action.currentRoute,
                    )
                    is BottomNavigationUiAction.GraphCompletedHandling -> onGraphCompletedHandling()
                    is BottomNavigationUiAction.GraphTakeResponsibility -> onGraphTakeResponsibility()
                }
            }
        }
    }
}
