package com.velord.bottomnavigation.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.collection.forEach
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Hexagon
import androidx.compose.material.icons.outlined.Settings
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.NavHostGraphSpec
import com.velord.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import com.velord.multiplebackstackapplier.utils.compose.SnackBarOnBackPressHandler
import com.velord.resource.R
import com.velord.uicore.compose.component.AnimatableLabeledIcon
import com.velord.util.context.getActivity
import kotlinx.coroutines.flow.filterNotNull
import org.koin.androidx.compose.koinViewModel

interface BottomNavigator {
    fun getDirection(route: BottomNavigationDestination): Direction
    fun getStartRoute(route: BottomNavigationDestination): String
    fun getGraph(): NavHostGraphSpec
    @Composable fun CreateDestinationsNavHostForBottom(
        navController: NavHostController,
        modifier: Modifier,
        startRoute: BottomNavigationDestination
    )
}

enum class BottomNavigationDestination {
    Camera,
    Demo,
    Settings;
}

@Destination<ExternalModuleGraph>()
@Composable
fun BottomNavigationDestination(
    navigator: BottomNavigator
) {
    val viewModel = koinViewModel<BottomNavigationDestinationsVM>()

    val tabState = viewModel.currentTabFlow.collectAsStateWithLifecycle()
    val backHandlingState = viewModel.backHandlingStateFlow.collectAsStateWithLifecycle()
    val finishAppEventState = viewModel.finishAppEvent.collectAsStateWithLifecycle(initialValue = null)

    val context = LocalContext.current
    LaunchedEffect(finishAppEventState) {
        snapshotFlow { finishAppEventState.value }
            .filterNotNull()
            .collect {
                context.getActivity()?.finish()
            }
    }

    SideEffect {
        // When the graph is completed, we can proceed with the back handling
        // Current logic is simple, we just allow the back handling
        viewModel.graphCompletedHandling()
    }

    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    LaunchedEffect(currentDestination) {
        if (currentDestination == null) return@LaunchedEffect
        val nodes = mutableListOf<NavDestination>()

        navController.graph.nodes.forEach { _, value ->
            nodes.add(value)
        }
        val startDestinationRoster = nodes.map {
            when(it) {
                is NavGraph -> it.startDestinationRoute
                else -> it.route
            }
        }
        viewModel.updateBackHandling(startDestinationRoster, currentDestination)
    }

    LaunchedEffect(key1 = tabState) {
        snapshotFlow { tabState.value }.collect { tab ->
            onTabClick(
                isSelected = tab.isSame,
                item = tab.current,
                navController = navController,
                navigator = navigator,
                onClick = {}
            )
        }
    }

    Content(
        tab = tabState.value.current,
        createNavHost = {
            navigator.CreateDestinationsNavHostForBottom(
                navController = navController,
                modifier = Modifier
                    .padding(bottom = it.calculateBottomPadding())
                    .fillMaxSize(),
                startRoute = tabState.value.current
            )
        },
        getNavigationItems = viewModel::getNavigationItems,
        onTabClick = viewModel::onTabClick,
    )

    Log.d("Navigation", "startRouteRoster: ${backHandlingState.value}")
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
    tab: BottomNavigationDestination,
    createNavHost: @Composable (PaddingValues) -> Unit,
    getNavigationItems: () -> List<BottomNavigationDestination>,
    onTabClick: (BottomNavigationDestination) -> Unit,
) {
    Scaffold(
        bottomBar = {
            BottomBar(
                tabs = getNavigationItems(),
                selectedItem = tab,
                onClick = onTabClick,
            )
        },
        content = {
            createNavHost(it)
        },
    )
}

private fun onTabClick(
    isSelected: Boolean,
    item: BottomNavigationDestination,
    navController: NavController,
    navigator: BottomNavigator,
    onClick: (BottomNavigationDestination) -> Unit
) {
    if (navController.currentDestination?.route == navigator.getStartRoute(item)) return
    if (isSelected) {
        // When we click again on a bottom bar item and it was already selected
        // we want to pop the back stack until the initial destination of this bottom bar item
        navController.popBackStack(navigator.getStartRoute(item), false)
        return
    }

    navController.navigate(navigator.getDirection(item).route) {
        // Pop up to the root of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(navigator.getGraph().route) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
    onClick(item)
}

@Composable
private fun BottomBar(
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
                onClick = { onClick(item) },
                label = {},
                icon = {
                    val color = MaterialTheme.colorScheme.run {
                        if (isSelected) secondary else onSurface
                    }
                    val painter = rememberVectorPainter(image = item.getIcon())
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

@Composable
private fun BottomNavigationDestination.getIcon(): ImageVector = when (this) {
    BottomNavigationDestination.Camera -> Icons.Outlined.Camera
    BottomNavigationDestination.Demo -> Icons.Outlined.Hexagon
    BottomNavigationDestination.Settings -> Icons.Outlined.Settings
}