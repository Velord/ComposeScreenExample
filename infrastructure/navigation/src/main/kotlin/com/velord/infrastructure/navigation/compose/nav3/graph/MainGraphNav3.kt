package com.velord.infrastructure.navigation.compose.nav3.graph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.velord.infrastructure.navigation.compose.nav3.GraphNav3
import com.velord.infrastructure.navigation.compose.nav3.navigator.SupremeNavigatorNav3
import com.velord.ui.feature.bottomnavigation.screen.compose.BottomNavigationScreen
import com.velord.ui.feature.setting.SettingScreen
import com.velord.ui.sharedviewmodel.ThemeVM
import org.koin.compose.viewmodel.koinViewModel

internal fun EntryProviderScope<NavKey>.setupMainGraphNav3(navigator: SupremeNavigatorNav3)  {
    entry<GraphNav3.Main.BottomNavigationDestinationNav3> {
        BottomNavigationScreen(navigator = navigator)
    }

    entry<GraphNav3.Main.SettingDestinationNav3> {
        val viewModel = koinViewModel<ThemeVM>()
        SettingScreen(
            viewModel = viewModel,
            onBackClick = {
               // Handle back navigation if needed
            }
        )
    }
}
