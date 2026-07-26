package com.velord.ui.feature.bottomnavigation.screen.voyager

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Hexagon
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.velord.core.navigation.voyager.SharedScreenVoyager
import com.velord.core.resource.Res
import com.velord.core.resource.camera
import com.velord.core.resource.demo
import com.velord.core.resource.settings
import org.jetbrains.compose.resources.stringResource

/*
Each tab owns a nested Voyager Navigator. The bottom-navigation parent provides this callback so
the active tab can report its navigator and neutral route identities upward. The Navigator stays
inside the Voyager adapter, while the VM receives only route identities. This keeps back ownership
based on the active tab stack instead of the outer navigator that hosts the bottom-navigation
screen.
*/
internal val LocalVoyagerNavigatorObserver =
    staticCompositionLocalOf<(Navigator, List<String?>, String?) -> Unit> {
        error("LocalVoyagerNavigatorObserver is not provided")
    }

sealed class VoyagerBottomNavigationTab : Tab {

    data object Camera : VoyagerBottomNavigationTab() {

        override val options: TabOptions
            @Composable
            get() {
                val title = stringResource(Res.string.camera)
                val icon = rememberVectorPainter(image = Icons.Outlined.Camera)

                return remember {
                    TabOptions(
                        index = 0u,
                        title = title,
                        icon = icon
                    )
                }
            }

        @Composable
        override fun Content() {
            val screen = rememberScreen(SharedScreenVoyager.BottomNavigationTab.Camera)
            Navigator(screen) { navigator ->
                ObserveNavigator(
                    navigator = navigator,
                    startDestination = screen,
                )
                CurrentScreen()
            }
        }
    }

    data object Demo : VoyagerBottomNavigationTab() {

        override val options: TabOptions
            @Composable
            get() {
                val title = stringResource(Res.string.demo)
                val icon = rememberVectorPainter(image = Icons.Outlined.Hexagon)

                return remember {
                    TabOptions(
                        index = 1u,
                        title = title,
                        icon = icon
                    )
                }
            }

        @Composable
        override fun Content() {
            val screen = rememberScreen(SharedScreenVoyager.BottomNavigationTab.Demo)
            Navigator(screen) { navigator ->
                ObserveNavigator(
                    navigator = navigator,
                    startDestination = screen,
                )
                SlideTransition(navigator)
            }
        }
    }

    data object Settings : VoyagerBottomNavigationTab() {

        override val options: TabOptions
            @Composable
            get() {
                val title = stringResource(Res.string.settings)
                val icon = rememberVectorPainter(image = Icons.Outlined.Settings)

                return remember {
                    TabOptions(
                        index = 2u,
                        title = title,
                        icon = icon
                    )
                }
            }

        @Composable
        override fun Content() {
            val screen = rememberScreen(SharedScreenVoyager.BottomNavigationTab.Settings)
            Navigator(screen) { navigator ->
                ObserveNavigator(
                    navigator = navigator,
                    startDestination = screen,
                )
                CurrentScreen()
            }
        }
    }
}

@Composable
private fun ObserveNavigator(navigator: Navigator, startDestination: Screen) {
    val onNavigatorChanged = LocalVoyagerNavigatorObserver.current
    val currentDestination = navigator.lastItem
    LaunchedEffect(currentDestination) {
        onNavigatorChanged(
            navigator,
            listOf(startDestination.key),
            currentDestination.key,
        )
    }
}
