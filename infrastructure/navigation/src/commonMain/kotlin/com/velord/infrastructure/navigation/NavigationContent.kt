package com.velord.infrastructure.navigation

import androidx.compose.runtime.Composable
import com.velord.infrastructure.config.NavigationLib
import com.velord.infrastructure.navigation.creation.CreateNavigationViaDestinations
import com.velord.infrastructure.navigation.creation.CreateNavigationViaJetpack
import com.velord.infrastructure.navigation.creation.CreateNavigationViaNav3
import com.velord.infrastructure.navigation.creation.CreateNavigationViaVanilla
import com.velord.infrastructure.navigation.creation.CreateNavigationViaVoyager

@Composable
internal fun NavigationContent(navigationLib: NavigationLib) {
    when (navigationLib) {
        NavigationLib.Nav3 -> CreateNavigationViaNav3()
        NavigationLib.Voyager -> CreateNavigationViaVoyager()
        NavigationLib.Jetpack -> CreateNavigationViaJetpack()
        NavigationLib.Destinations -> CreateNavigationViaDestinations()
        NavigationLib.Compose -> CreateNavigationViaVanilla()
    }
}
