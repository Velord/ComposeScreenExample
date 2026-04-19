package com.velord.bottomnavigation.screen.voyager

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Hexagon
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.registry.rememberScreen
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

sealed class BottomNavigationTab : Tab {

    data object Camera : BottomNavigationTab() {
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
            Navigator(screen)
        }
    }

    data object Demo : BottomNavigationTab() {
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
            Navigator(screen) {
                SlideTransition(it)
            }
        }
    }

    data object Settings : BottomNavigationTab() {
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
            Navigator(screen)
        }
    }
}
