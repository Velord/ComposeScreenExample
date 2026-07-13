package com.velord.infrastructure.navigation.compose.nav3.graph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.velord.infrastructure.navigation.compose.nav3.GraphNav3
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import com.velord.ui.feature.camerarecording.CameraRecordingNavigationEvent
import com.velord.ui.feature.camerarecording.CameraRecordingNavigator
import com.velord.ui.feature.camerarecording.CameraRecordingScreen
import com.velord.ui.feature.camerarecording.CameraRecordingVM
import org.koin.androidx.compose.koinViewModel

internal fun EntryProviderScope<NavKey>.setupCameraRecordingGraphNav3(navigator: CameraRecordingNavigator)  {
    entry<GraphNav3.BottomTab.CameraRecording.CameraRecordingDestinationNav3> {
        val viewModel = koinViewModel<CameraRecordingVM>()
        val bottomNavVM = koinViewModel<BottomNavigationDestinationsVM>()
        CameraRecordingScreen(
            viewModel = viewModel,
            needToHandlePermission = true,
            onNavigationEvent = {
                when (it) {
                    CameraRecordingNavigationEvent.SETTINGS -> navigator.goToSettingFromCameraRecording()
                }
            },
            onBackClick = {
                bottomNavVM.graphCompletedHandling()
            }
        )
    }
}
