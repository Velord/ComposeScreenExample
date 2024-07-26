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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import com.ramcosta.composedestinations.utils.route
import com.velord.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import com.velord.bottomnavigation.viewmodel.TabState
import com.velord.multiplebackstackapplier.utils.compose.SnackBarOnBackPressHandler
import com.velord.resource.R
import com.velord.uicore.compose.component.AnimatableLabeledIcon
import com.velord.uicore.utils.ObserveSharedFlow
import com.velord.util.context.getActivity
import org.koin.androidx.compose.koinViewModel

@SuppressLint("RestrictedApi")
@Composable
internal fun LogBackStack(navController: NavController) {
    LaunchedEffect(navController) {
        navController.currentBackStack.collect {
            it.print()
        }
    }
}

private fun Collection<NavBackStackEntry>.print(prefix: String = "stack") {
    val stack = toMutableList()
        .map {
            val route = it.route()
            val args = runCatching { route.argsFrom(it) }.getOrNull()?.takeIf { it != Unit }?.let { "(args={$it})" } ?: ""
            "$route$args"
        }
        .toTypedArray().contentToString()
    Log.d("LogBackStack", "$prefix = $stack")
}

@Destination<ExternalModuleGraph>()
@Composable
fun BottomNavigationComposeScreen(
    content: @Composable (
        viewModel: BottomNavigationDestinationsVM,
        navController: NavHostController,
        currentTab: BottomNavigationItem,
    ) -> Unit,
) {
    val viewModel = koinViewModel<BottomNavigationDestinationsVM>()
    val backHandlingState = viewModel.backHandlingStateFlow.collectAsStateWithLifecycle()
    val tabState = viewModel.currentTabFlow.collectAsStateWithLifecycle(TabState.Default)

    val context = LocalContext.current
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    //LogBackStack(navController = navController)

    ObserveSharedFlow(flow = viewModel.finishAppEvent) {
        context.getActivity()?.finish()
    }

    SideEffect {
        // When the graph is completed, we can proceed with the back handling
        // Current logic is simple, we just allow the back handling
        viewModel.graphCompletedHandling()
    }

    LaunchedEffect(currentDestination) {
        if (currentDestination == null) return@LaunchedEffect
        val nodes = mutableListOf<NavDestination>()

        navController.graph.nodes.forEach { _, value ->
            nodes.add(value)
        }
        val startDestinationRoster = nodes.map {
            when (it) {
                is NavGraph -> it.startDestinationRoute
                else -> it.route
            }
        }
        viewModel.updateBackHandling(startDestinationRoster, currentDestination)
    }

    content(viewModel, navController, tabState.value.current)

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
internal fun Content(
    tab: BottomNavigationItem,
    createNavHost: @Composable (PaddingValues) -> Unit,
    getNavigationItems: () -> List<BottomNavigationItem>,
    onTabClick: (BottomNavigationItem) -> Unit,
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

@Composable
private fun BottomBar(
    tabs: List<BottomNavigationItem>,
    selectedItem: BottomNavigationItem,
    onClick: (BottomNavigationItem) -> Unit,
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
private fun BottomNavigationItem.getIcon(): ImageVector = when (this) {
    BottomNavigationItem.Camera -> Icons.Outlined.Camera
    BottomNavigationItem.Demo -> Icons.Outlined.Hexagon
    BottomNavigationItem.Settings -> Icons.Outlined.Settings
}