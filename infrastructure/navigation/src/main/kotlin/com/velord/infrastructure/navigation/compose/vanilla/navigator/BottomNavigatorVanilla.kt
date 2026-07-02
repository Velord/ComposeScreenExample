package com.velord.infrastructure.navigation.compose.vanilla.navigator

import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import com.velord.ui.feature.camerarecording.CameraRecordingNavigator
import com.velord.ui.feature.demo.DemoDestinationNavigationEvent
import com.velord.ui.feature.demo.DemoNavigator
import com.velord.infrastructure.navigation.compose.vanilla.GraphVanilla

private val log = Logger.withTag("LogBackStack - BottomNavigatorVanilla")

internal class BottomNavigatorVanilla(
    private val parent: SupremeNavigatorVanilla,
    private val navController: NavHostController
) : DemoNavigator, CameraRecordingNavigator by parent {

    init {
        log.d { "init: ${this.navController}" }
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
