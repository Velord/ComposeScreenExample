package com.velord.ui.feature.bottomnavigation.screen.component

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.velord.core.ui.compose.component.AnimatableLabeledIcon
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem

@Composable
internal fun Content(
    selectedItem: BottomNavigationItem,
    navigationItemRoster: List<BottomNavigationItem>,
    onTabClick: (BottomNavigationItem) -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        bottomBar = {
            BottomBar(
                selectedItem = selectedItem,
                navigationItemRoster = navigationItemRoster,
                modifier = Modifier
                    .navigationBarsPadding()
                    .height(72.dp),
                onClick = onTabClick,
            )
        },
        content = content,
    )
}

@Composable
internal fun BottomBar(
    selectedItem: BottomNavigationItem,
    navigationItemRoster: List<BottomNavigationItem>,
    modifier: Modifier = Modifier,
    onClick: (BottomNavigationItem) -> Unit,
) {
    NavigationBar(modifier = modifier) {
        navigationItemRoster.forEach { item ->
            val isSelected = selectedItem == item
            NavigationBarItem(
                selected = isSelected,
                onClick = { onClick(item) },
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
                        iconSize = 28.dp,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                        LocalAbsoluteTonalElevation.current,
                    ),
                ),
            )
        }
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    Content(
        selectedItem = BottomNavigationItem.Camera,
        navigationItemRoster = BottomNavigationItem.entries,
        onTabClick = {},
        content = {},
    )
}
