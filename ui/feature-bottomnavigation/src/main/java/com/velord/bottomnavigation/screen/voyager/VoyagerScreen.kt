package com.velord.bottomnavigation.screen.voyager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.velord.bottomnavigation.screen.compose.BottomBar
import com.velord.bottomnavigation.viewmodel.BottomNavigationItem
import com.velord.bottomnavigation.viewmodel.BottomNavigationVoyagerVM
import com.velord.core.resource.R
import com.velord.multiplebackstackapplier.utils.compose.SnackBarOnBackPressHandler
import com.velord.util.context.getActivity

private fun BottomNavigationItem.toTab(): BottomNavigationTab = when (this) {
    BottomNavigationItem.Camera -> BottomNavigationTab.Camera
    BottomNavigationItem.Demo -> BottomNavigationTab.Demo
    BottomNavigationItem.Setting -> BottomNavigationTab.Settings
}

@Composable
fun VoyagerScreen(viewModel: BottomNavigationVoyagerVM) {
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
        currentItem = tabState.value,
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

@Composable
private fun Content(
    currentItem: BottomNavigationItem,
    getNavigationItems: () -> List<BottomNavigationItem>,
    onTabClick: (BottomNavigationItem) -> Unit,
) {
    val tab = currentItem.toTab()
    TabNavigator(tab) {
        val tabNavigator = LocalTabNavigator.current
        LaunchedEffect(tab) {
            tabNavigator.current = tab
        }

        Scaffold(
            bottomBar = {
                Surface {
                    BottomBar(
                        tabs = getNavigationItems(),
                        selectedItem = currentItem,
                        onClick = onTabClick,
                    )
                }
            },
            content = { padding ->
                Surface(Modifier.padding(bottom = padding.calculateBottomPadding())) {
                    CurrentTab()
                }
            },
        )
    }
}