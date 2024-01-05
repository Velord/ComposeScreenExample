package com.velord.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class SharedScreen : ScreenProvider {
    sealed class BottomNavigationTab : SharedScreen() {
        data object Camera : BottomNavigationTab()
        data object Demo : BottomNavigationTab()
        data object Settings : BottomNavigationTab()
    }
    data object Test : SharedScreen()
}