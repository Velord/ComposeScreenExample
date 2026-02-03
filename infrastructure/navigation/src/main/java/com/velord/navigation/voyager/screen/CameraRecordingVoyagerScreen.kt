package com.velord.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import com.velord.camerarecording.CameraRecordingNavigationEvent
import com.velord.camerarecording.CameraRecordingScreen
import com.velord.camerarecording.CameraRecordingViewModel
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
            onNavigationEvent = {},
            onBackClick = {
                bottomNavViewModel.graphCompletedHandling()
            }
        )
    }
}