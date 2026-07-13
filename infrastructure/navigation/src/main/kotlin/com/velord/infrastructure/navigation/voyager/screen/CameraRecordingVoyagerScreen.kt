package com.velord.infrastructure.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.core.navigation.voyager.ObserveNavigation
import com.velord.ui.feature.bottomnavigation.viewmodel.destinations.BottomNavigationDestinationsUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.destinations.BottomNavigationDestinationsVM
import com.velord.ui.feature.camerarecording.CameraRecordingScreen
import com.velord.ui.feature.camerarecording.CameraRecordingVM
import org.koin.compose.viewmodel.koinViewModel

internal object CameraRecordingVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<CameraRecordingVM>()
        val bottomNavVM = koinViewModel<BottomNavigationDestinationsVM>()

        ObserveNavigation(viewModel.navigationEventVoyager)

        CameraRecordingScreen(
            viewModel = viewModel,
            needToHandlePermission = true,
            onNavigationEvent = {}, // Handled by ObserveNavigation
            onBackClick = {
                bottomNavVM.onAction(BottomNavigationDestinationsUiAction.GraphCompletedHandling)
            }
        )
    }
}
