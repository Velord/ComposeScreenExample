package com.velord.infrastructure.navigation.compose.vanilla.navigator

import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import com.velord.infrastructure.navigation.compose.vanilla.GraphVanilla
import com.velord.ui.feature.camerarecording.CameraRecordingNavigator
import com.velord.ui.feature.demo.DemoNavigationEvent
import com.velord.ui.feature.demo.DemoNavigator

private val log = Logger.withTag("LogBackStack - BottomNavigatorVanilla")

internal class BottomNavigatorVanilla(
    private val parent: SupremeNavigatorVanilla,
    private val navController: NavHostController,
) : DemoNavigator, CameraRecordingNavigator by parent {

    init {
        log.d { "init: ${this.navController}" }
    }

    override fun goTo(dest: DemoNavigationEvent) {
        navController.navigate(dest.toGraphVanilla())
    }

    internal companion object {
        fun DemoNavigationEvent.toGraphVanilla(): Any = when(this) {
            DemoNavigationEvent.Shape -> GraphVanilla.BottomTab.Demo.ShapeDemoDestinationVanilla
            DemoNavigationEvent.Modifier -> GraphVanilla.BottomTab.Demo.ModifierDestinationVanilla
            DemoNavigationEvent.FlowSummator ->
                GraphVanilla.BottomTab.Demo.FlowSummatorDestinationVanilla
            DemoNavigationEvent.Morph -> GraphVanilla.BottomTab.Demo.MorphDemoDestinationVanilla
            DemoNavigationEvent.HintPhoneNumber ->
                GraphVanilla.BottomTab.Demo.HintPhoneDestinationVanilla
            DemoNavigationEvent.Movie -> GraphVanilla.BottomTab.Demo.MovieDestinationVanilla
            DemoNavigationEvent.Dialog -> GraphVanilla.BottomTab.Demo.DialogDestinationVanilla
        }
    }
}
