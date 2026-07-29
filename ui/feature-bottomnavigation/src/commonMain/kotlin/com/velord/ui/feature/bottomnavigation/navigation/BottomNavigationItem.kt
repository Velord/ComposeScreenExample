package com.velord.ui.feature.bottomnavigation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Hexagon
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.velord.core.resource.Res
import com.velord.core.resource.camera
import com.velord.core.resource.demo
import com.velord.core.resource.settings
import org.jetbrains.compose.resources.StringResource

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

    val label: StringResource get() = when (this) {
        Camera -> Res.string.camera
        Demo -> Res.string.demo
        Setting -> Res.string.settings
    }
}
