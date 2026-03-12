package com.velord.bottomnavigation.screen.jetpack

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Hexagon
import androidx.compose.material.icons.outlined.Settings
import com.velord.core.resource.R
import com.velord.multiplebackstackapplier.MultipleBackstackGraphItem
import com.velord.resource.R

enum class BottomNavigationItem(
    override val navigationGraphId: Int,
    override val startDestinationId: Int
) : MultipleBackstackGraphItem {
    Camera(R.id.left_nav_graph, R.id.bottom_nav_graph_left_start_destination),
    Demo(R.id.center_nav_graph, R.id.bottom_nav_graph_center_start_destination),
<<<<<<<< HEAD:feature-bottomnavigation/src/main/java/com/velord/bottomnavigation/BottomNavigationItem.kt
    Settings(R.id.right_nav_graph, R.id.bottom_nav_graph_right_start_destination);
========
    Setting(R.id.right_nav_graph, R.id.bottom_nav_graph_right_start_destination);
>>>>>>>> 76f71e457c730912e8e00a3beb96f602e7765555:ui/feature-bottomnavigation/src/main/java/com/velord/bottomnavigation/screen/jetpack/BottomNavigationItem.kt


    val icon get() = when (this) {
        Camera -> Icons.Outlined.Camera
        Demo -> Icons.Outlined.Hexagon
<<<<<<<< HEAD:feature-bottomnavigation/src/main/java/com/velord/bottomnavigation/BottomNavigationItem.kt
        Settings -> Icons.Outlined.Settings
========
        Setting -> Icons.Outlined.Settings
>>>>>>>> 76f71e457c730912e8e00a3beb96f602e7765555:ui/feature-bottomnavigation/src/main/java/com/velord/bottomnavigation/screen/jetpack/BottomNavigationItem.kt
    }
}