package com.velord.composescreenexample.utils.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Settings
import com.velord.composescreenexample.R

enum class BottomNavigationItem(override val navigationGraphId: Int) : MultipleBackstackGraphItem {
    Camera(R.id.camera_nav_graph),
    Add(R.id.add_nav_graph),
    Settings(R.id.settings_nav_graph);


    val icon get() = when (this) {
        Camera -> Icons.Outlined.Camera
        Add -> Icons.Outlined.Add
        Settings -> Icons.Outlined.Settings
    }
}