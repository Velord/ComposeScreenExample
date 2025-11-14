package com.velord.navigation.compose.vanilla.navigator

import android.util.Log
import androidx.navigation.NavHostController
import com.velord.camerarecording.CameraRecordingNavigator
import com.velord.feature.demo.DemoDestinationNavigationEvent
import com.velord.feature.demo.DemoNavigator
import com.velord.navigation.compose.vanilla.GraphVanilla

internal class BottomNavigatorVanilla(
    private val parent: SupremeNavigatorVanilla,
    private val navController: NavHostController
) : DemoNavigator, CameraRecordingNavigator by parent {

    init {
        Log.d("LogBackStack - BottomNavigator", "init: ${this.navController}")
    }

    override fun goTo(dest: DemoDestinationNavigationEvent) {
        val dest = when(dest) {
            DemoDestinationNavigationEvent.Shape -> GraphVanilla.BottomTab.Demo.ShapeDemoDestinationVanilla
            DemoDestinationNavigationEvent.Modifier -> GraphVanilla.BottomTab.Demo.ModifierDestinationVanilla
            DemoDestinationNavigationEvent.FlowSummator -> GraphVanilla.BottomTab.Demo.FlowSummatorDestinationVanilla
            DemoDestinationNavigationEvent.Morph -> GraphVanilla.BottomTab.Demo.MorphDemoDestinationVanilla
            DemoDestinationNavigationEvent.HintPhoneNumber -> GraphVanilla.BottomTab.Demo.HintPhoneDestinationVanilla
            DemoDestinationNavigationEvent.Movie -> GraphVanilla.BottomTab.Demo.MovieDestinationVanilla
            DemoDestinationNavigationEvent.Dialog -> GraphVanilla.BottomTab.Demo.DialogDestinationVanilla
        }
        navController.navigate(dest)
    }
}