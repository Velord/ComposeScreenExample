package com.velord.ui.feature.bottomnavigation.viewmodel.destinations

import com.velord.ui.feature.bottomnavigation.navigation.BottomNavBackHandlingState
import com.velord.ui.feature.bottomnavigation.navigation.TabState

data class BottomNavigationDestinationsUiState(
    val tabState: TabState,
    val backHandlingState: BottomNavBackHandlingState,
)
