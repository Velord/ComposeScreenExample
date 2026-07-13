package com.velord.ui.feature.bottomnavigation.viewmodel

import androidx.navigation.NavDestination
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem

sealed interface BottomNavigationJetpackUiAction {
    data class TabClick(val newTab: BottomNavigationItem) : BottomNavigationJetpackUiAction
    data object BackDoubleClick : BottomNavigationJetpackUiAction
    data class ShowBackPressToast(val tag: String) : BottomNavigationJetpackUiAction
    data class UpdateBackHandling(
        val currentNavigationDestination: NavDestination?,
    ) : BottomNavigationJetpackUiAction
    data object GraphCompletedHandling : BottomNavigationJetpackUiAction
    data object GraphTakeResponsibility : BottomNavigationJetpackUiAction
}
