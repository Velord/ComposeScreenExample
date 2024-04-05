package com.velord.bottomnavigation.screen

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.velord.bottomnavigation.BottomNavigationItem
import com.velord.bottomnavigation.TAG
import com.velord.bottomnavigation.viewmodel.BottomNavViewModelJetpack
import com.velord.multiplebackstackapplier.utils.compose.SnackBarOnBackPressHandler
import com.velord.resource.R
import com.velord.uicore.compose.component.AnimatableLabeledIcon
import com.velord.uicore.compose.preview.PreviewCombined

@Composable
fun BottomNavigationJetpackScreen(viewModel: BottomNavViewModelJetpack) {
    val tabFlow = viewModel.currentTabFlow.collectAsStateWithLifecycle()
    val backHandlingState = viewModel.backHandlingStateFlow.collectAsStateWithLifecycle()

    Content(
        selectedItem = tabFlow.value,
        onClick = viewModel::onTabClick,
    )

    Log.d(TAG, "BottomNavScreen: ${backHandlingState.value}")
    if (backHandlingState.value.isEnabled) {
        val str = stringResource(id = R.string.press_again_to_exit)
        SnackBarOnBackPressHandler(
            message = str,
            modifier = Modifier.padding(horizontal = 8.dp),
            enabled = backHandlingState.value.isEnabled,
            onBackClickLessThanDuration = viewModel::onBackDoubleClick,
        ) {
            Snackbar {
                Text(text = it.visuals.message)
            }
        }
    }
}

@Composable
private fun Content(
    selectedItem: BottomNavigationItem,
    onClick: (BottomNavigationItem) -> Unit,
) {
    NavigationBar {
        BottomNavigationItem.entries.forEach {
            val isSelected = selectedItem == it
            NavigationBarItem(
                selected = isSelected,
                onClick = { onClick(it) },
                label = {},
                icon = {
                    val color = MaterialTheme.colorScheme.run {
                        if (isSelected) secondary else onSurface
                    }
                    val painter = rememberVectorPainter(image = it.icon)
                    AnimatableLabeledIcon(
                        label = it.name,
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

@PreviewCombined
@Composable
private fun BottomNavContentPreview() {
    Content(
        selectedItem = BottomNavigationItem.Camera,
        onClick = {},
    )
}