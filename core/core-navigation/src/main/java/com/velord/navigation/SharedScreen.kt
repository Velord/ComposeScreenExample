package com.velord.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class SharedScreen : ScreenProvider {

    sealed class BottomNavigationTab : SharedScreen() {
        data object Camera : BottomNavigationTab()
        data object Demo : BottomNavigationTab()
        data object Settings : BottomNavigationTab()
    }

    data class Test(
        @StringRes val title: Int,
        val modifier: Modifier = Modifier,
        val onClick: () -> Unit = {},
    ) : SharedScreen()

    sealed class Demo : SharedScreen() {
        data object Shape : Demo()
        data object Modifier : Demo()
        data object FlowSummator : Demo()
        data object Morph : Demo()
    }
}