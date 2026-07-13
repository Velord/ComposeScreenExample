package com.velord.ui.feature.bottomnavigation.screen.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.velord.ui.feature.bottomnavigation.BottomNavigationItem
import com.velord.ui.feature.bottomnavigation.TabState

interface BottomNavigator {
    fun onTabClick(tab: TabState)
    @Composable fun CreateNavHostForBottom(
        modifier: Modifier,
        startRoute: BottomNavigationItem
    )
    @Composable
    fun SetupNavController(
        updateBackHandling: (startDestinationRoster: List<String?>, currentRoute: String?) -> Unit,
        onTabChanged: (BottomNavigationItem) -> Unit,
    )
}
