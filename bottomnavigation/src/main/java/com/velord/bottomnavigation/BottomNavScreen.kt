package com.velord.bottomnavigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.velord.uicore.compose.component.AnimatableLabeledIcon

object BottomNavScreen : Screen {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    override fun Content() {
        TabNavigator(BottomNavigationTab.Camera) { tab ->
            val tabNavigator = LocalTabNavigator.current

            Scaffold(
                content = {
                    CurrentTab()
                },
                bottomBar = {
                    BottomBar(
                        selectedItem = tabNavigator.current as BottomNavigationTab,
                        onClick = { tab ->
                            tabNavigator.current = tab
                        }
                    )
                }
            )
        }
    }

    @Composable
    private fun BottomBar(
        selectedItem: BottomNavigationTab,
        onClick: (BottomNavigationTab) -> Unit,
    ) {
        NavigationBar(
            modifier = Modifier
                .navigationBarsPadding()
                .height(72.dp),
        ) {
            BottomNavigationTab::class.sealedSubclasses.forEach { subClass ->
                val it = subClass.objectInstance as BottomNavigationTab
                val isSelected = selectedItem == it
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onClick(it) },
                    label = {},
                    icon = {
                        val color = MaterialTheme.colorScheme.run {
                            if (isSelected) secondary else onSurface
                        }
                        AnimatableLabeledIcon(
                            label = (it as Tab).options.title,
                            painter = it.options.icon!!,
                            scale = if (isSelected) 1.5f else 1f,
                            color = color,
                            modifier = Modifier,
                            iconSize = 28.dp,
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                            LocalAbsoluteTonalElevation.current
                        )
                    )
                )
            }
        }
    }
}