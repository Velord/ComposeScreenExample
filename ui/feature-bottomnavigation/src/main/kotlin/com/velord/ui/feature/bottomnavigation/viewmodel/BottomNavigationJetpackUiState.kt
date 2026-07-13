package com.velord.ui.feature.bottomnavigation.viewmodel

import com.velord.ui.feature.bottomnavigation.navigation.BottomNavBackHandlingState
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem

data class BottomNavigationJetpackUiState(
    val currentTab: BottomNavigationItem,
    val backHandlingState: BottomNavBackHandlingState,
)
