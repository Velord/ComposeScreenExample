package com.velord.infrastructure.navigation.compose.destinations.navigator

import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import com.ramcosta.composedestinations.generated.navigation.destinations.DialogDestinationDestination
import com.ramcosta.composedestinations.generated.navigation.destinations.FlowSummatorDestinationDestination
import com.ramcosta.composedestinations.generated.navigation.destinations.HintPhoneNumberDestinationDestination
import com.ramcosta.composedestinations.generated.navigation.destinations.ModifierDemoDestinationDestination
import com.ramcosta.composedestinations.generated.navigation.destinations.MorphDemoDestinationDestination
import com.ramcosta.composedestinations.generated.navigation.destinations.MovieDestinationDestination
import com.ramcosta.composedestinations.generated.navigation.destinations.ShapeDemoDestinationDestination
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import com.velord.ui.feature.camerarecording.CameraRecordingNavigator
import com.velord.ui.feature.demo.DemoNavigationEvent
import com.velord.ui.feature.demo.DemoNavigator

private val log = Logger.withTag("LogBackStack - BottomNavigatorDestinations")

internal class BottomNavigatorDestinations(
    private val parent: SupremeNavigatorDestinations,
    private val navController: NavHostController
) : DemoNavigator, CameraRecordingNavigator by parent {

    init {
        log.d { "init: ${this.navController}" }
    }

    override fun goTo(dest: DemoNavigationEvent) {
        val dest = when(dest) {
            DemoNavigationEvent.Shape -> ShapeDemoDestinationDestination
            DemoNavigationEvent.Modifier -> ModifierDemoDestinationDestination
            DemoNavigationEvent.FlowSummator -> FlowSummatorDestinationDestination
            DemoNavigationEvent.Morph -> MorphDemoDestinationDestination
            DemoNavigationEvent.HintPhoneNumber -> HintPhoneNumberDestinationDestination
            DemoNavigationEvent.Movie -> MovieDestinationDestination
            DemoNavigationEvent.Dialog -> DialogDestinationDestination
        }
        navController.toDestinationsNavigator().navigate(dest)
    }
}
