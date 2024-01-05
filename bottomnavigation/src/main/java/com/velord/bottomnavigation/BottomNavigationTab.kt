package com.velord.bottomnavigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Hexagon
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.velord.navigation.SharedScreen
import com.velord.resource.R

sealed class BottomNavigationTab : Tab {

    data object Camera : BottomNavigationTab() {
        override val options: TabOptions
            @Composable
            get() {
                val title = stringResource(id = R.string.camera)
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
            Text(
                text = stringResource(id = R.string.camera),
                modifier = Modifier.padding(16.dp),
                color = Color.Blue
            )
        }
    }

    data object Demo : BottomNavigationTab() {
        override val options: TabOptions
            @Composable
            get() {
                val title = stringResource(id = R.string.demo)
                val icon = rememberVectorPainter(image = Icons.Outlined.Hexagon)

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
            Text(
                text = stringResource(id = R.string.demo),
                modifier = Modifier.padding(16.dp),
                color = Color.Blue
            )
        }
    }

    data object Settings : BottomNavigationTab() {
        override val options: TabOptions
            @Composable
            get() {
                val title = stringResource(id = R.string.settings)
                val icon = rememberVectorPainter(image = Icons.Outlined.Settings)

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
            val navigator = LocalNavigator.currentOrThrow
            val screen = rememberScreen(SharedScreen.BottomNavigationTab.Settings)

            navigator.push(screen)
        }
    }
}