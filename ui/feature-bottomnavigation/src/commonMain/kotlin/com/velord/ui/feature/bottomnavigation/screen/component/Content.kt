package com.velord.ui.feature.bottomnavigation.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.velord.core.ui.compose.component.AnimatableLabeledIcon
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem
import org.jetbrains.compose.resources.stringResource

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
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
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
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    ) {
        navigationItemRoster.forEach { item ->
            val isSelected = selectedItem == item
            val activeColor = Color(0xFFA87BF7)
            val inactiveColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            val tabColor = if (isSelected) activeColor else inactiveColor
            val glowColor = activeColor.takeIf { isSelected }
            val painter = rememberVectorPainter(image = item.icon)
            val label = stringResource(item.label)

            NavigationBarItem(
                selected = isSelected,
                onClick = { onClick(item) },
                label = {},
                icon = {
                    AnimatableLabeledIcon(
                        label = label,
                        painter = painter,
                        scale = 1f,
                        color = tabColor,
                        iconSize = 28.dp,
                        glowColor = glowColor,
                        contentDescription = label,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = activeColor,
                    unselectedIconColor = inactiveColor,
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
