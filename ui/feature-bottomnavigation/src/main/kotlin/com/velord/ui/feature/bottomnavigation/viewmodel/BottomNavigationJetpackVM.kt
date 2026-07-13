package com.velord.ui.feature.bottomnavigation.viewmodel

import androidx.navigation.NavDestination
import com.velord.core.resource.R
import com.velord.core.resource.Res
import com.velord.core.resource.bottom_navigation_first_back_press
import com.velord.model.ToastConfig
import com.velord.model.ToastDuration
import com.velord.ui.feature.bottomnavigation.BottomNavEventService
import com.velord.ui.feature.bottomnavigation.screen.jetpack.BottomNavigationItem
import com.velord.ui.sharedviewmodel.CoroutineScopeViewModel
import com.velord.usecase.event.ShowToastUC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

class BottomNavigationJetpackVM(
    private val bottomNavEventService: BottomNavEventService,
    private val showToastUC: ShowToastUC,
): CoroutineScopeViewModel() {

    val currentTabStateFlow = MutableStateFlow(BottomNavigationItem.Setting)
    val backHandlingStateFlow = bottomNavEventService.backHandlingStateFlow
    val finishAppEvent: MutableSharedFlow<Unit> = MutableSharedFlow()

    private val graphBackHandlerToTab = listOf(
        R.id.settingsFragment to BottomNavigationItem.Setting,
        R.id.demoFragment to BottomNavigationItem.Demo,
        R.id.cameraRecordingFragment to BottomNavigationItem.Camera,
    )

    fun getNavigationItems() = BottomNavigationItem.entries

    fun onTabClick(newTab: BottomNavigationItem) {
        val current = currentTabStateFlow.value
        if (current == newTab) return

        currentTabStateFlow.value = newTab
    }

    fun onBackDoubleClick() = launch {
        finishAppEvent.emit(Unit)
    }

    fun showBackPressToast(tag: String) = launch {
        val message = getString(Res.string.bottom_navigation_first_back_press, tag)
        val toastConfig = ToastConfig(message = message, duration = ToastDuration.Short)
        showToastUC(toastConfig)
    }

    fun updateBackHandling(currentNavigationDestination: NavDestination?) {
        val isStart = currentNavigationDestination.isCurrentStartDestination(graphBackHandlerToTab)
        val newState = backHandlingStateFlow.value.copy(isAtStartGraphDestination = isStart)
        bottomNavEventService.updateBackHandlingState(newState)
    }

    private fun NavDestination?.isCurrentStartDestination(
        items: List<Pair<Int, BottomNavigationItem>>,
    ): Boolean = items.firstOrNull { it.first == this?.id }?.second == currentTabStateFlow.value

    fun graphCompletedHandling() {
        changeGrantedToProceed(true)
    }

    fun graphTakeResponsibility() {
        changeGrantedToProceed(false)
    }

    private fun changeGrantedToProceed(isGranted: Boolean) {
        val newState = backHandlingStateFlow.value.copy(isGrantedToProceed = isGranted)
        bottomNavEventService.updateBackHandlingState(newState)
    }
}
