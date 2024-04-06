package com.velord.bottomnavigation.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Hexagon
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import com.ramcosta.composedestinations.generated.destinations.CameraScreenDestination
import com.ramcosta.composedestinations.generated.destinations.DemoScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SettingsScreenDestination
import com.velord.bottomnavigation.BottomNavEventService
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

enum class BottomNavigationDestination(
    val direction: String,
    val icon: ImageVector
)  {
    Camera(
        CameraScreenDestination.route,
        Icons.Outlined.Camera
    ),
    Demo(
        DemoScreenDestination.route,
        Icons.Outlined.Hexagon
    ),
    Settings(
        SettingsScreenDestination.route,
        Icons.Outlined.Settings
    );
}

@KoinViewModel
class BottomNavigationDestinationsVM(
    private val bottomNavEventService: BottomNavEventService
): CoroutineScopeViewModel() {

    val currentTabFlow = MutableStateFlow(BottomNavigationDestination.Demo)
    val backHandlingStateFlow = bottomNavEventService.backHandlingStateFlow
    val finishAppEvent: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun onTabClick(newTab: BottomNavigationDestination) {
        if (newTab == currentTabFlow.value) return
        currentTabFlow.value = newTab
    }

    fun onBackDoubleClick() = launch {
        finishAppEvent.emit(true)
    }

    fun getNavigationItems() = BottomNavigationDestination.entries

    fun updateBackHandling(dest: NavDestination?) {
       // val isStart = currentNavigationDestination.isCurrentStartDestination(graphBackHandlerToTab)
        val newState = backHandlingStateFlow.value.copy(isAtStartGraphDestination = false)
        bottomNavEventService.updateBackHandlingState(newState)
    }
}