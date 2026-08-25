package com.velord.ui.feature.bottomnavigation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Hexagon
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.velord.core.resource.AppString
import com.velord.core.resource.AppStringResource

enum class BottomNavigationItem {
    Camera,
    Demo,
    Setting;

    val icon: ImageVector get() = when (this) {
        Camera -> Icons.Outlined.Camera
        Demo -> Icons.Outlined.Hexagon
        Setting -> Icons.Outlined.Settings
    }

    val isCamera: Boolean get() = this == Camera
    val isDemo: Boolean get() = this == Demo
    val isSetting: Boolean get() = this == Setting

    val label: AppStringResource get() = when (this) {
        Camera -> AppString.camera
        Demo -> AppString.demo
        Setting -> AppString.settings
    }
}
