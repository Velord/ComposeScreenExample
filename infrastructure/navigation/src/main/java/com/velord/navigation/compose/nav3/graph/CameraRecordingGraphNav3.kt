package com.velord.navigation.compose.nav3.graph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.velord.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import com.velord.camerarecording.CameraRecordingNavigationEvent
import com.velord.camerarecording.CameraRecordingNavigator
import com.velord.camerarecording.CameraRecordingScreen
import com.velord.camerarecording.CameraRecordingViewModel
import com.velord.navigation.compose.nav3.GraphNav3
import org.koin.androidx.compose.koinViewModel

internal fun EntryProviderScope<NavKey>.setupCameraRecordingGraphNav3(navigator: CameraRecordingNavigator)  {
    entry<GraphNav3.BottomTab.CameraRecording.CameraRecordingDestinationNav3> {
        val viewModel = koinViewModel<CameraRecordingViewModel>()
        val bottomNavViewModel = koinViewModel<BottomNavigationDestinationsVM>()
        CameraRecordingScreen(
            viewModel = viewModel,
            needToHandlePermission = true,
            onNavigationEvent = {
                when (it) {
                    CameraRecordingNavigationEvent.SETTINGS -> navigator.goToSettingFromCameraRecording()
                }
            },
            onBackClick = {
                bottomNavViewModel.graphCompletedHandling()
            }
        )
    }
}