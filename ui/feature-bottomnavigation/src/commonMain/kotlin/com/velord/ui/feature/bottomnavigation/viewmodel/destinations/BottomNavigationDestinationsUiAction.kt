package com.velord.ui.feature.bottomnavigation.viewmodel.destinations

import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem

sealed interface BottomNavigationDestinationsUiAction {
    data class TabClick(val newTab: BottomNavigationItem) : BottomNavigationDestinationsUiAction
    data class TabDestinationChanged(
        val newTab: BottomNavigationItem,
    ) : BottomNavigationDestinationsUiAction
    data object BackDoubleClick : BottomNavigationDestinationsUiAction
    data class UpdateBackHandling(
        val startDestinationRoster: List<String?>,
        val currentRoute: String?,
    ) : BottomNavigationDestinationsUiAction
    data object GraphCompletedHandling : BottomNavigationDestinationsUiAction
    data object GraphTakeResponsibility : BottomNavigationDestinationsUiAction
}
