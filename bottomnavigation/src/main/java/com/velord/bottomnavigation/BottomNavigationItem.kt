package com.velord.bottomnavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Settings
import com.velord.multiplebackstackapplier.MultipleBackstackGraphItem

enum class BottomNavigationItem(
    override val navigationGraphId: Int,
    override val startDestinationId: Int
) : MultipleBackstackGraphItem {
    Camera(R.id.left_nav_graph, R.id.bottom_nav_graph_left_start_destination),
    Add(R.id.center_nav_graph, R.id.bottom_nav_graph_center_start_destination),
    Settings(R.id.right_nav_graph, R.id.bottom_nav_graph_right_start_destination);


    val icon get() = when (this) {
        Camera -> Icons.Outlined.Camera
        Add -> Icons.Outlined.Add
        Settings -> Icons.Outlined.Settings
    }
}