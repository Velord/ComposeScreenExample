package com.velord.composescreenexample.ui.main.bottomNav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.velord.composescreenexample.R
import com.velord.composescreenexample.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

enum class BottomNavItem {
    Camera,
    Add,
    Settings;


    val icon get() = when (this) {
        Camera -> Icons.Outlined.Camera
        Add -> Icons.Outlined.Add
        Settings -> Icons.Outlined.Settings
    }

    val navigationId get() = when (this) {
        Camera -> R.id.cameraRecordingFragment
        Add -> R.id.inDevelopmentFragment
        Settings -> R.id.inDevelopmentFragment
    }
}

@HiltViewModel
class BottomNavViewModel @Inject constructor(
) : BaseViewModel() {

    val tabFlow: MutableStateFlow<BottomNavItem> = MutableStateFlow(BottomNavItem.Camera)

    fun onTabClick(newTab: BottomNavItem) {
        if (newTab == tabFlow.value) return
        tabFlow.value = newTab
    }
}