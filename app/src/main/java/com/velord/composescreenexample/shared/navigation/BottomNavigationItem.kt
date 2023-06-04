package com.velord.composescreenexample.shared.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Settings
import com.velord.composescreenexample.R
import com.velord.multiplebackstackapplier.MultipleBackstackGraphItem

enum class BottomNavigationItem(
    override val navigationGraphId: Int,
    override val startDestinationId: Int
) : MultipleBackstackGraphItem {
    Camera(R.id.camera_nav_graph, R.id.cameraGraphFragment),
    Add(R.id.add_nav_graph, R.id.addGraphFragment),
    Settings(R.id.settings_nav_graph, R.id.settingsGraphFragment);


    val icon get() = when (this) {
        Camera -> Icons.Outlined.Camera
        Add -> Icons.Outlined.Add
        Settings -> Icons.Outlined.Settings
    }
}