package com.velord.infrastructure.navigation.creation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.velord.infrastructure.navigation.voyager.initVoyager
import com.velord.ui.feature.bottomnavigation.screen.voyager.BottomNavigationVoyagerScreen

@Composable
internal fun CreateNavigationViaVoyager() {
    initVoyager()
    Navigator(BottomNavigationVoyagerScreen) {
        SlideTransition(it)
    }
}
