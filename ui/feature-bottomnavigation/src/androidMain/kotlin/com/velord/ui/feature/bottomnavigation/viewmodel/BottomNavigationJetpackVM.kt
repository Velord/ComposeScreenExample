package com.velord.ui.feature.bottomnavigation.viewmodel

import androidx.navigation.NavDestination
import com.velord.core.resource.R
import com.velord.core.resource.Res
import com.velord.core.resource.bottom_navigation_first_back_press
import com.velord.model.ToastConfig
import com.velord.model.ToastDuration
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavBackHandlingState
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavEventService
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem
import com.velord.ui.feature.bottomnavigation.navigation.TabState
import com.velord.ui.sharedviewmodel.CoroutineScopeVM
import com.velord.usecase.event.RequestAppExitUC
import com.velord.usecase.event.ShowToastUC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

class BottomNavigationJetpackVM(
    private val bottomNavEventService: BottomNavEventService,
    private val requestAppExitUC: RequestAppExitUC,
    private val showToastUC: ShowToastUC,
) : CoroutineScopeVM() {

    val uiStateFlow = MutableStateFlow(
        BottomNavigationJetpackUiState(
            tabState = bottomNavEventService.currentTabStateFlow.value,
            backHandlingState = bottomNavEventService.backHandlingStateFlow.value,
        )
    )
    private val actionFlow = MutableSharedFlow<BottomNavigationJetpackUiAction>()

    private val graphBackHandlerToTab = listOf(
        R.id.settingsFragment to BottomNavigationItem.Setting,
        R.id.demoFragment to BottomNavigationItem.Demo,
        R.id.cameraRecordingFragment to BottomNavigationItem.Camera,
    )

    init {
        observe()
    }

    fun onAction(action: BottomNavigationJetpackUiAction) {
        launch {
            actionFlow.emit(action)
        }
    }

    private fun onTabClick(newTab: BottomNavigationItem) {
        val currentTabState = uiStateFlow.value.tabState
        if (currentTabState.current == newTab) return

        val newTabState = TabState(previous = currentTabState.current, current = newTab)
        bottomNavEventService.updateTab(newTabState)
        uiStateFlow.update { state -> state.copy(tabState = newTabState) }
    }

    private fun onBackDoubleClick() = launch { requestAppExitUC() }

    private fun onShowBackPressToast(tag: String) = launch {
        val message = getString(Res.string.bottom_navigation_first_back_press, tag)
        val toastConfig = ToastConfig(message = message, duration = ToastDuration.Short)
        showToastUC(toastConfig)
    }

    private fun onUpdateBackHandling(currentNavigationDestination: NavDestination?) {
        val isStart = currentNavigationDestination.isCurrentStartDestination(graphBackHandlerToTab)
        val newState = uiStateFlow.value.backHandlingState.copy(isAtStartGraphDestination = isStart)
        updateBackHandlingState(newState)
    }

    private fun NavDestination?.isCurrentStartDestination(
        items: List<Pair<Int, BottomNavigationItem>>,
    ): Boolean = items
        .firstOrNull { it.first == this?.id }
        ?.second == uiStateFlow.value.tabState.current

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
                    is BottomNavigationJetpackUiAction.TabClick -> onTabClick(action.newTab)
                    is BottomNavigationJetpackUiAction.BackDoubleClick -> onBackDoubleClick()
                    is BottomNavigationJetpackUiAction.ShowBackPressToast ->
                        onShowBackPressToast(action.tag)
                    is BottomNavigationJetpackUiAction.UpdateBackHandling ->
                        onUpdateBackHandling(action.currentNavigationDestination)
                    is BottomNavigationJetpackUiAction.GraphCompletedHandling ->
                        onGraphCompletedHandling()
                    is BottomNavigationJetpackUiAction.GraphTakeResponsibility ->
                        onGraphTakeResponsibility()
                }
            }
        }
    }
}
