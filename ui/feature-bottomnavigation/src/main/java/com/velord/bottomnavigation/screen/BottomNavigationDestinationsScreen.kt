package com.velord.bottomnavigation.screen

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.DemoScreenDestination
import com.ramcosta.composedestinations.navigation.popUpTo
import com.velord.bottomnavigation.viewmodel.BottomNavigationDestination
import com.velord.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import com.velord.multiplebackstackapplier.utils.compose.SnackBarOnBackPressHandler
import com.velord.resource.R
import com.velord.uicore.compose.component.AnimatableLabeledIcon
import com.velord.util.context.getActivity
import org.koin.androidx.compose.koinViewModel

@Destination<ExternalModuleGraph>(start = true)
@Composable
fun BottomNavigationDestinationsScreen() {
    val viewModel = koinViewModel<BottomNavigationDestinationsVM>()

    val tabState = viewModel.currentTabFlow.collectAsStateWithLifecycle()
    val backHandlingState = viewModel.backHandlingStateFlow.collectAsStateWithLifecycle()
    val finishAppEventState = viewModel.finishAppEvent.collectAsStateWithLifecycle(initialValue = false)

    val context = LocalContext.current
    LaunchedEffect(finishAppEventState) {
        if (finishAppEventState.value) {
            context.getActivity()?.finish()
        }
    }

    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination
    Log.d("@@@", "currentDestination: $currentDestination")
    LaunchedEffect(currentDestination) {
        viewModel.updateBackHandling(currentDestination)
    }

    Content(
        navigator = navController,
        tab = tabState.value,
        getNavigationItems = viewModel::getNavigationItems,
        onTabClick = viewModel::onTabClick,
    )

    val str = stringResource(id = R.string.press_again_to_exit)
    SnackBarOnBackPressHandler(
        message = str,
        modifier = Modifier.padding(horizontal = 8.dp),
        enabled = backHandlingState.value.isEnabled,
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
    navigator: NavHostController,
    tab: BottomNavigationDestination,
    getNavigationItems: () -> List<BottomNavigationDestination>,
    onTabClick: (BottomNavigationDestination) -> Unit,
) {
    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navigator,
                tabs = getNavigationItems(),
                selectedItem = tab,
                onClick = onTabClick,
            )
        },
        content = {
            DestinationsNavHost(
                navController = navigator,
                navGraph = NavGraphs.bottomNavigationGraph,
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                startRoute = DemoScreenDestination,
            )
        },
    )
}

@Composable
private fun BottomBar(
    navController: NavController,
    tabs: List<BottomNavigationDestination>,
    selectedItem: BottomNavigationDestination,
    onClick: (BottomNavigationDestination) -> Unit,
) {
    NavigationBar (
        modifier = Modifier
            .navigationBarsPadding()
            .height(72.dp),
    ) {
        tabs.forEach { item ->
            val isSelected = selectedItem == item
            //val isCurrentDestOnBackStack = navController.isRouteOnBackStack(item.direction)
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (isSelected) {
                        // When we click again on a bottom bar item and it was already selected
                        // we want to pop the back stack until the initial destination of this bottom bar item
                        navController.popBackStack(item.direction, false)
                        return@NavigationBarItem
                    }

                    navController.navigate(item.direction) {
                        // Pop up to the root of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(NavGraphs.bottomNavigationGraph) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                    onClick(item)
                },
                label = {},
                icon = {
                    val color = MaterialTheme.colorScheme.run {
                        if (isSelected) secondary else onSurface
                    }
                    val painter = rememberVectorPainter(image = item.icon)
                    AnimatableLabeledIcon(
                        label = item.name,
                        painter = painter,
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