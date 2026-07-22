package com.velord.ui.feature.bottomnavigation.viewmodel

import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem

sealed interface BottomNavigationUiAction {
    data class TabClick(val newTab: BottomNavigationItem) : BottomNavigationUiAction
    data object BackDoubleClick : BottomNavigationUiAction
    data class UpdateBackHandling(
        val startDestinationRoster: List<String?>,
        val currentRoute: String?,
    ) : BottomNavigationUiAction
    data class TabDestinationChanged(
        val newTab: BottomNavigationItem,
    ) : BottomNavigationUiAction
    data object GraphCompletedHandling : BottomNavigationUiAction
    data object GraphTakeResponsibility : BottomNavigationUiAction
}
