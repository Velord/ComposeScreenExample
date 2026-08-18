package com.velord.ui.feature.bottomnavigation.viewmodel

import com.velord.core.resource.AppString
import com.velord.core.resource.getString
import com.velord.model.ToastConfig
import com.velord.model.ToastDuration
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavBackHandlingState
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavEventService
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem
import com.velord.ui.feature.bottomnavigation.navigation.TabState
import com.velord.ui.sharedviewmodel.CoroutineScopeVM
import com.velord.usecase.event.RequestAppExitUC
import com.velord.usecase.event.ShowToastUC
import com.velord.usecase.setting.GetLocalizationStateUC
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private const val CONFIRM_EXIT_THROTTLE = 2000L

class BottomNavigationVM(
    private val bottomNavEventService: BottomNavEventService,
    private val requestAppExitUC: RequestAppExitUC,
    private val showToastUC: ShowToastUC,
    getLocalizationStateUC: GetLocalizationStateUC,
) : CoroutineScopeVM() {

    private val localizationStateFlow = getLocalizationStateUC()

    val uiStateFlow = MutableStateFlow(BottomNavigationUiState.from(bottomNavEventService))
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

    fun getNavigationItemRoster() = BottomNavigationItem.entries

    private suspend fun onTabDestinationChanged(newTab: BottomNavigationItem) {
        if (uiStateFlow.value.tabState.current == newTab) return
        onTabClick(newTab)
    }

    private suspend fun onTabClick(newTab: BottomNavigationItem) {
        val current = uiStateFlow.value.tabState
        val new = current.copy(previous = current.current, current = newTab)
        bottomNavEventService.updateTab(new)
        uiStateFlow.update { state ->
            state.copy(
                tabState = new,
                isConfirmExitRequested = false,
            )
        }
        bottomNavEventService.emitTabClick(new)
    }

    private fun onBackDoubleClick() = launch {
        requestAppExitUC()
    }

    private suspend fun onBackClick() = onTabClick(TabState.DEFAULT.current)

    private suspend fun onBackRequest() {
        when (uiStateFlow.value.backBehavior) {
            BottomNavigationBackBehavior.DelegateToNavigator -> {
                if (uiStateFlow.value.backHandlingState.isAtStartGraphDestination) {
                    onBackDoubleClick()
                }
            }
            BottomNavigationBackBehavior.ReturnToDefaultTab -> onBackClick()
            BottomNavigationBackBehavior.ConfirmExit -> confirmExitBackRequest()
        }
    }

    private suspend fun confirmExitBackRequest() {
        if (uiStateFlow.value.isConfirmExitRequested) {
            onBackDoubleClick()
        } else {
            setConfirmExitRequested(true)
            val localization = requireNotNull(localizationStateFlow.value) {
                "Localization is not initialized"
            }
            val message = getString(localization, AppString.press_again_to_exit)
            val toastConfig = ToastConfig(
                message = message,
                duration = ToastDuration.Short,
            )
            showToastUC(toastConfig)
        }
    }

    private fun setConfirmExitRequested(isRequested: Boolean) {
        bottomNavEventService.updateConfirmExitRequested(isRequested)
    }

    private fun onUpdateBackHandling(
        startDestinationRoster: List<String?>,
        currentRoute: String?
    ) {
        val isStart = startDestinationRoster.contains(currentRoute)
        val newState = uiStateFlow.value.backHandlingState.copy(
            isAtStartGraphDestination = isStart,
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
        uiStateFlow.update { state ->
            state.copy(
                backHandlingState = newState,
                isConfirmExitRequested = if (newState.isEnabled) {
                    state.isConfirmExitRequested
                } else {
                    false
                },
            )
        }
    }

    private suspend fun handleUiAction(action: BottomNavigationUiAction) {
        when (action) {
            is BottomNavigationUiAction.TabClick -> onTabClick(action.newTab)
            is BottomNavigationUiAction.TabDestinationChanged -> onTabDestinationChanged(action.newTab)
            is BottomNavigationUiAction.BackDoubleClick -> onBackDoubleClick()
            is BottomNavigationUiAction.BackClick -> onBackClick()
            is BottomNavigationUiAction.BackRequest -> onBackRequest()
            is BottomNavigationUiAction.UpdateBackHandling -> onUpdateBackHandling(
                startDestinationRoster = action.startDestinationRoster,
                currentRoute = action.currentRoute,
            )
            is BottomNavigationUiAction.GraphCompletedHandling -> onGraphCompletedHandling()
            is BottomNavigationUiAction.GraphTakeResponsibility -> onGraphTakeResponsibility()
        }
    }

    private fun observe() {
        launch {
            bottomNavEventService.currentTabStateFlow.collect { tabState ->
                uiStateFlow.update { state -> state.copy(tabState = tabState) }
            }
        }
        launch {
            bottomNavEventService.backHandlingStateFlow.collect { backHandlingState ->
                uiStateFlow.update { state -> state.copy(backHandlingState = backHandlingState) }
            }
        }
        launch {
            bottomNavEventService.confirmExitRequestedFlow.collect { isConfirmExitRequested ->
                uiStateFlow.update { state -> state.copy(isConfirmExitRequested = isConfirmExitRequested) }
            }
        }
        launch {
            bottomNavEventService.onTabClickEvent.collect { tabState ->
                onTabClickEvent.emit(tabState)
            }
        }
        launch {
            actionFlow.collect { action ->
                handleUiAction(action)
            }
        }
        launch {
            uiStateFlow.filter { it.isConfirmExitRequested }.collectLatest {
                delay(CONFIRM_EXIT_THROTTLE.milliseconds)
                setConfirmExitRequested(false)
            }
        }
        launch {
            uiStateFlow.map { it.backBehavior }.filterNot { it.isConfirmExit }.collectLatest {
                setConfirmExitRequested(false)
            }
        }
    }
}
