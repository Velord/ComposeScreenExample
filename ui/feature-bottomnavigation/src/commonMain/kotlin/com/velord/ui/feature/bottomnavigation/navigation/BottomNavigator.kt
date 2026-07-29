package com.velord.ui.feature.bottomnavigation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface BottomNavigator {
    fun onTabClick(tab: TabState)
    @Composable
    fun CreateNavHostForBottom(
        modifier: Modifier,
        startRoute: BottomNavigationItem,
    )
    @Composable
    fun SetupNavController(
        updateBackHandling: (startDestinationRoster: List<String?>, currentRoute: String?) -> Unit,
        onTabChanged: (BottomNavigationItem) -> Unit,
    )
}