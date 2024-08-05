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
import com.velord.feature.demo.DemoDest
import com.velord.feature.demo.DemoNavigator

internal class BottomNavigatorDestinations(
    private val parent: SupremeNavigatorDestinations,
    private val navController: NavHostController
) : DemoNavigator, CameraRecordingNavigator by parent {

    init {
        Log.d("LogBackStack - BottomNavigator", "init: ${this.navController}")
    }

    override fun goTo(dest: DemoDest) {
        val dest = when(dest) {
            DemoDest.Shape -> ShapeDemoDestinationDestination
            DemoDest.Modifier -> ModifierDemoDestinationDestination
            DemoDest.FlowSummator -> FlowSummatorDestinationDestination
            DemoDest.Morph -> MorphDemoDestinationDestination
            DemoDest.HintPhoneNumber -> HintPhoneNumberDestinationDestination
            DemoDest.Movie -> MovieDestinationDestination
        }
        navController.toDestinationsNavigator().navigate(dest)
    }
}