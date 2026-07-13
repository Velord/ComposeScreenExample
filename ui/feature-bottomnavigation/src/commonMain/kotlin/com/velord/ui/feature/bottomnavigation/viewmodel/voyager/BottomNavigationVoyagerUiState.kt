package com.velord.ui.feature.bottomnavigation.viewmodel.voyager

import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem

data class BottomNavigationVoyagerUiState(
    val currentTab: BottomNavigationItem,
    val isBackHandlingEnabled: Boolean,
) {
    companion object {
        val DEFAULT = BottomNavigationVoyagerUiState(
            currentTab = BottomNavigationItem.Camera,
            isBackHandlingEnabled = true,
        )
    }
}
