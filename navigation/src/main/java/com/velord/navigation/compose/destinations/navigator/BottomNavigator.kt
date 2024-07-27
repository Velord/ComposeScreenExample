package com.velord.navigation.compose.destinations.navigator

import android.util.Log
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.generated.destinations.FlowSummatorDestinationDestination
import com.ramcosta.composedestinations.generated.destinations.HintPhoneNumberDestinationDestination
import com.ramcosta.composedestinations.generated.destinations.ModifierDemoDestinationDestination
import com.ramcosta.composedestinations.generated.destinations.MorphDemoDestinationDestination
import com.ramcosta.composedestinations.generated.destinations.MovieDestinationDestination
import com.ramcosta.composedestinations.generated.destinations.ShapeDemoDestinationDestination
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import com.velord.camerarecording.CameraRecordingNavigator
import com.velord.feature.demo.DemoDest
import com.velord.feature.demo.DemoNavigator

internal class BottomNavigator(
    private val parent: SupremeNavigator,
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