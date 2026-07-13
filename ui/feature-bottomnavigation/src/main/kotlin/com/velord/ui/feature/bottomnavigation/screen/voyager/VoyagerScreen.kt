package com.velord.ui.feature.bottomnavigation.screen.voyager

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.velord.core.resource.Res
import com.velord.core.resource.press_again_to_exit
import com.velord.infrastructure.util.context.getActivity
import com.velord.multiplebackstackapplier.utils.compose.SnackBarOnBackPressHandler
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem
import com.velord.ui.feature.bottomnavigation.screen.compose.BottomBar
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationVoyagerUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationVoyagerVM
import org.jetbrains.compose.resources.stringResource

private fun BottomNavigationItem.toTab(): BottomNavigationTab = when (this) {
    BottomNavigationItem.Camera -> BottomNavigationTab.Camera
    BottomNavigationItem.Demo -> BottomNavigationTab.Demo
    BottomNavigationItem.Setting -> BottomNavigationTab.Settings
}

@Composable
fun VoyagerScreen(viewModel: BottomNavigationVoyagerVM) {
    val uiState = viewModel.uiStateFlow.collectAsStateWithLifecycle()
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
        viewModel.onAction(BottomNavigationVoyagerUiAction.UpdateBackHandling(lastItem))
    }

    Content(
        currentItem = uiState.value.currentTab,
        getNavigationItems = viewModel::getNavigationItems,
        onTabClick = { tab ->
            viewModel.onAction(BottomNavigationVoyagerUiAction.TabClick(tab))
        },
    )

    val str = stringResource(Res.string.press_again_to_exit)
    SnackBarOnBackPressHandler(
        message = str,
        modifier = Modifier.padding(horizontal = 8.dp),
        enabled = uiState.value.isBackHandlingEnabled,
        onBackClickLessThanDuration = {
            viewModel.onAction(BottomNavigationVoyagerUiAction.BackDoubleClick)
        },
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
