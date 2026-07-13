package com.velord.ui.feature.bottomnavigation.viewmodel

import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem

data class BottomNavigationVoyagerUiState(
    val currentTab: BottomNavigationItem,
    val isBackHandlingEnabled: Boolean,
)
