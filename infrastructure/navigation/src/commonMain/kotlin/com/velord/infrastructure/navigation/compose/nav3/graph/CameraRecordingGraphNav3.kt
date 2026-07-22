package com.velord.infrastructure.navigation.compose.nav3.graph

import androidx.navigation3.runtime.EntryProviderScope
import com.velord.infrastructure.navigation.compose.nav3.GraphNav3
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationVM
import com.velord.ui.feature.camerarecording.CameraRecordingNavigationEvent
import com.velord.ui.feature.camerarecording.CameraRecordingNavigator
import com.velord.ui.feature.camerarecording.CameraRecordingScreen
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingVM
import org.koin.compose.viewmodel.koinViewModel

internal fun EntryProviderScope<GraphNav3>.setupCameraRecordingGraphNav3(
    navigator: CameraRecordingNavigator,
) {
    entry<GraphNav3.BottomTab.CameraRecording.CameraRecordingDestinationNav3> {
        val viewModel = koinViewModel<CameraRecordingVM>()
        val bottomNavVM = koinViewModel<BottomNavigationVM>()
        CameraRecordingScreen(
            viewModel = viewModel,
            needToHandlePermission = true,
            onNavigationEvent = {
                when (it) {
                    CameraRecordingNavigationEvent.Setting ->
                        navigator.goToSettingFromCameraRecording()
                }
            },
            onBackClick = {
                bottomNavVM.onAction(BottomNavigationUiAction.GraphCompletedHandling)
            }
        )
    }
}
