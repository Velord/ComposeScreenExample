package com.velord.infrastructure.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import com.velord.ui.feature.camerarecording.CameraRecordingScreen
import com.velord.ui.feature.camerarecording.CameraRecordingViewModel
import com.velord.core.navigation.voyager.ObserveNavigation
import org.koin.androidx.compose.koinViewModel

internal object CameraRecordingVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<CameraRecordingViewModel>()
        val bottomNavViewModel = koinViewModel<BottomNavigationDestinationsVM>()

        ObserveNavigation(viewModel.navigationEventVoyager)

        CameraRecordingScreen(
            viewModel = viewModel,
            needToHandlePermission = true,
            onNavigationEvent = {}, // Handled by ObserveNavigation
            onBackClick = {
                bottomNavViewModel.graphCompletedHandling()
            }
        )
    }
}
