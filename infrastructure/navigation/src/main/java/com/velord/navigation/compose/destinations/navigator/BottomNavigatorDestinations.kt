package com.velord.navigation.compose.destinations.navigator

import android.util.Log
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.generated.navigation.destinations.FlowSummatorDestinationDestination
import com.ramcosta.composedestinations.generated.navigation.destinations.HintPhoneNumberDestinationDestination
import com.ramcosta.composedestinations.generated.navigation.destinations.ModifierDemoDestinationDestination
import com.ramcosta.composedestinations.generated.navigation.destinations.MorphDemoDestinationDestination
import com.ramcosta.composedestinations.generated.navigation.destinations.MovieDestinationDestination
import com.ramcosta.composedestinations.generated.navigation.destinations.ShapeDemoDestinationDestination
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import com.velord.camerarecording.CameraRecordingNavigator
import com.velord.feature.demo.DemoDestinationNavigationEvent
import com.velord.feature.demo.DemoNavigator

internal class BottomNavigatorDestinations(
    private val parent: SupremeNavigatorDestinations,
    private val navController: NavHostController
) : DemoNavigator, CameraRecordingNavigator by parent {

    init {
        Log.d("LogBackStack - BottomNavigator", "init: ${this.navController}")
    }

    override fun goTo(dest: DemoDestinationNavigationEvent) {
        val dest = when(dest) {
            DemoDestinationNavigationEvent.Shape -> ShapeDemoDestinationDestination
            DemoDestinationNavigationEvent.Modifier -> ModifierDemoDestinationDestination
            DemoDestinationNavigationEvent.FlowSummator -> FlowSummatorDestinationDestination
            DemoDestinationNavigationEvent.Morph -> MorphDemoDestinationDestination
            DemoDestinationNavigationEvent.HintPhoneNumber -> HintPhoneNumberDestinationDestination
            DemoDestinationNavigationEvent.Movie -> MovieDestinationDestination
        }
        navController.toDestinationsNavigator().navigate(dest)
    }
}