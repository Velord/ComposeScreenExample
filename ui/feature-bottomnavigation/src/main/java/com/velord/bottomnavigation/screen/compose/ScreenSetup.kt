package com.velord.bottomnavigation.screen.compose

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.velord.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import com.velord.bottomnavigation.viewmodel.BottomNavigationItem
import com.velord.core.resource.R
import com.velord.core.ui.compose.component.AnimatableLabeledIcon
import com.velord.core.ui.utils.ObserveSharedFlow
import com.velord.multiplebackstackapplier.utils.compose.SnackBarOnBackPressHandler
import com.velord.util.context.getActivity

@Composable
internal fun ScreenSetup(
    viewModel: BottomNavigationDestinationsVM,
    content: @Composable () -> Unit,
) {
    val backHandlingState = viewModel.backHandlingStateFlow.collectAsStateWithLifecycle()

    // Log whenever the state changes
    LaunchedEffect(backHandlingState.value) {
        Log.d("LogBackStack", "ScreenSetup: State Changed -> $backHandlingState.value")
    }

    val isEnabledState = remember {
        derivedStateOf {
            val enabled = backHandlingState.value.isEnabled
            Log.d("LogBackStack", "ScreenSetup: Derived isEnabled -> $enabled")
            enabled
        }
    }

    val context = LocalContext.current
    ObserveSharedFlow(flow = viewModel.finishAppEvent) {
        context.getActivity()?.finish()
    }

    SideEffect {
        // When the graph is completed, we can proceed with the back handling
        // Current logic is simple, we just allow the back handling
        //viewModel.graphCompletedHandling()
    }

    content()

    Log.d("LogBackStack", "ScreenSetup: Parent BackHandler Registered. Enabled=${isEnabledState.value}")
    val str = stringResource(id = R.string.press_again_to_exit)
    SnackBarOnBackPressHandler(
        message = str,
        modifier = Modifier.padding(horizontal = 8.dp),
        enabled = isEnabledState.value,
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
internal fun BottomBar(
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
    BottomNavigationItem.Setting -> Icons.Outlined.Settings
}