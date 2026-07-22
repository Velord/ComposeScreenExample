package com.velord.ui.feature.bottomnavigation.viewmodel

import com.velord.ui.feature.bottomnavigation.navigation.BottomNavBackHandlingState
import com.velord.ui.feature.bottomnavigation.navigation.TabState

data class BottomNavigationUiState(
    val tabState: TabState,
    val backHandlingState: BottomNavBackHandlingState,
)
