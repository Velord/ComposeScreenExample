package com.velord.bottomnavigation.screen.voyager

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.velord.bottomnavigation.viewmodel.BottomNavigationVoyagerVM
import com.velord.multiplebackstackapplier.utils.compose.SnackBarOnBackPressHandler
import com.velord.resource.R
import com.velord.uicore.compose.component.AnimatableLabeledIcon
import com.velord.util.context.getActivity

@Composable
fun VoyagerContent(viewModel: BottomNavigationVoyagerVM) {
    val tabState = viewModel.currentTabFlow.collectAsStateWithLifecycle()
    val isBackHandlingEnabledState = viewModel.isBackHandlingEnabledFlow.collectAsStateWithLifecycle()
    val finishAppEventState = viewModel.finishAppEvent.collectAsStateWithLifecycle(initialValue = false)

    val context = LocalContext.current
    LaunchedEffect(finishAppEventState.value) {
        if (finishAppEventState.value) {
            context.getActivity()?.finish()
        }
    }

    val navigator = LocalNavigator.current
    val lastItem = navigator?.lastItemOrNull
    LaunchedEffect(lastItem) {
        viewModel.updateBackHandling(lastItem)
    }

    Content(
        tab = tabState.value,
        getNavigationItems = viewModel::getNavigationItems,
        onTabClick = viewModel::onTabClick,
    )

    val str = stringResource(id = R.string.press_again_to_exit)
    SnackBarOnBackPressHandler(
        message = str,
        modifier = Modifier.padding(horizontal = 8.dp),
        enabled = isBackHandlingEnabledState.value,
        onBackClickLessThanDuration = viewModel::onBackDoubleClick,
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Snackbar {
                Text(text = it.visuals.message)
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun Content(
    tab: BottomNavigationTab,
    getNavigationItems: () -> List<BottomNavigationTab>,
    onTabClick: (BottomNavigationTab) -> Unit,
) {
    TabNavigator(tab) {
        val tabNavigator = LocalTabNavigator.current
        LaunchedEffect(tab) {
            tabNavigator.current = tab
        }

        Scaffold(
            content = {
                CurrentTab()
            },
            bottomBar = {
                Surface {
                    BottomBar(
                        tabs = getNavigationItems(),
                        selectedItem = tab,
                        onClick = onTabClick,
                    )
                }
            }
        )
    }
}

@Composable
private fun BottomBar(
    tabs: List<BottomNavigationTab>,
    selectedItem: BottomNavigationTab,
    onClick: (BottomNavigationTab) -> Unit,
) {
    NavigationBar(
        modifier = Modifier
            .navigationBarsPadding()
            .height(72.dp),
    ) {
        tabs.forEach {
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