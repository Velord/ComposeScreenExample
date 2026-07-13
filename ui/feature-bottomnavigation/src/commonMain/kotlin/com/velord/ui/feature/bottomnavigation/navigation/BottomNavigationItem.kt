package com.velord.ui.feature.bottomnavigation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Hexagon
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavigationItem {
    Camera,
    Demo,
    Setting;

    val icon: ImageVector get() = when (this) {
        Camera -> Icons.Outlined.Camera
        Demo -> Icons.Outlined.Hexagon
        Setting -> Icons.Outlined.Settings
    }
}
