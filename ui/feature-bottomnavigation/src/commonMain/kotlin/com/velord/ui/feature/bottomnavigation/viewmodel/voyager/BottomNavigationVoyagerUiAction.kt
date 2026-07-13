package com.velord.ui.feature.bottomnavigation.viewmodel.voyager

import cafe.adriel.voyager.core.screen.Screen
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem

sealed interface BottomNavigationVoyagerUiAction {
    data class TabClick(val newTab: BottomNavigationItem) : BottomNavigationVoyagerUiAction
    data object BackDoubleClick : BottomNavigationVoyagerUiAction
    data class UpdateBackHandling(
        val currentNavigationDestination: Screen?,
    ) : BottomNavigationVoyagerUiAction
}
