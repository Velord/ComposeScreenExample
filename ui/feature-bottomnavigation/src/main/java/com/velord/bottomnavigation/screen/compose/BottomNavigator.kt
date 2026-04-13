package com.velord.bottomnavigation.screen.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.velord.bottomnavigation.viewmodel.BottomNavigationItem
import com.velord.bottomnavigation.viewmodel.TabState

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
