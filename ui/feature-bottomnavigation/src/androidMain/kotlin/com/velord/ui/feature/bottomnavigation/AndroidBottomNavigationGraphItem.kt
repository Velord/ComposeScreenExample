package com.velord.ui.feature.bottomnavigation

import com.velord.core.resource.R
import com.velord.multiplebackstackapplier.MultipleBackstackGraphItem
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem

private data class AndroidBottomNavigationGraphItem(
    override val navigationGraphId: Int,
    override val startDestinationId: Int,
) : MultipleBackstackGraphItem

internal fun BottomNavigationItem.toMultipleBackstackGraphItem(): MultipleBackstackGraphItem {
    val navigationGraphId = when (this) {
        BottomNavigationItem.Camera -> R.id.left_nav_graph
        BottomNavigationItem.Demo -> R.id.center_nav_graph
        BottomNavigationItem.Setting -> R.id.right_nav_graph
    }
    val startDestinationId = when (this) {
        BottomNavigationItem.Camera -> R.id.bottom_nav_graph_left_start_destination
        BottomNavigationItem.Demo -> R.id.bottom_nav_graph_center_start_destination
        BottomNavigationItem.Setting -> R.id.bottom_nav_graph_right_start_destination
    }

    return AndroidBottomNavigationGraphItem(
        navigationGraphId = navigationGraphId,
        startDestinationId = startDestinationId,
    )
}
