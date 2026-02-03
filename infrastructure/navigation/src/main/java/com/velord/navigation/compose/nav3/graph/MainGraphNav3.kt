package com.velord.navigation.compose.nav3.graph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.velord.bottomnavigation.screen.compose.BottomNavigationScreen
import com.velord.navigation.compose.nav3.GraphNav3
import com.velord.navigation.compose.nav3.navigator.SupremeNavigatorNav3
import com.velord.setting.SettingScreen
import com.velord.sharedviewmodel.ThemeViewModel
import org.koin.androidx.compose.koinViewModel

internal fun EntryProviderScope<NavKey>.setupMainGraphNav3(navigator: SupremeNavigatorNav3)  {
    entry<GraphNav3.Main.BottomNavigationDestinationNav3> {
        BottomNavigationScreen(navigator = navigator)
    }

    entry<GraphNav3.Main.SettingDestinationNav3> {
        val viewModel = koinViewModel<ThemeViewModel>()
        SettingScreen(
            viewModel = viewModel,
            onBackClick = {
               // Handle back navigation if needed
            }
        )
    }
}