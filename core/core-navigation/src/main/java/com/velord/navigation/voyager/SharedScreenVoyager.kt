package com.velord.navigation.voyager

import androidx.annotation.StringRes
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class SharedScreenVoyager : ScreenProvider {

    sealed class BottomNavigationTab : SharedScreenVoyager() {
        data object Camera : BottomNavigationTab()
        data object Demo : BottomNavigationTab()
        data object Settings : BottomNavigationTab()
    }

    data class Test(
        @StringRes val title: Int,
        val modifier: Modifier = Modifier,
        val onClick: () -> Unit = {},
    ) : SharedScreenVoyager()

    sealed class Demo : SharedScreenVoyager() {
        data object Shape : Demo()
        data object Modifier : Demo()
        data object FlowSummator : Demo()
        data object Morph : Demo()
        data object HintPhoneNumber : Demo()
        data object Movie : Demo()
        data object Dialog : Demo()
    }
}