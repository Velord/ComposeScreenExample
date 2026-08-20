package com.velord.infrastructure.navigation.compose.nav3.navigator

import co.touchlab.kermit.Logger
import com.velord.infrastructure.navigation.compose.nav3.GraphNav3
import com.velord.ui.feature.camerarecording.CameraRecordingNavigator
import com.velord.ui.feature.demo.DemoNavigationEvent
import com.velord.ui.feature.demo.DemoNavigator

private val log = Logger.withTag("LogBackStack - BottomNavigatorNav3")

internal class BottomNavigatorNav3(
    private val parent: SupremeNavigatorNav3,
    private val backStackNavigator: BackStackNavigator,
) : DemoNavigator, CameraRecordingNavigator by parent {

    init {
        log.d { "init: ${this.backStackNavigator}" }
    }

    override fun goTo(dest: DemoNavigationEvent) {
        backStackNavigator.navigate(dest.toGraphNav3())
    }

    override fun goBack() {
        backStackNavigator.goBack()
    }

    internal companion object {
        fun DemoNavigationEvent.toGraphNav3(): GraphNav3.BottomTab.Demo = when (this) {
            DemoNavigationEvent.Shape -> GraphNav3.BottomTab.Demo.ShapeDemoDestinationNav3
            DemoNavigationEvent.Modifier -> GraphNav3.BottomTab.Demo.ModifierDestinationNav3
            DemoNavigationEvent.FlowSummator ->
                GraphNav3.BottomTab.Demo.FlowSummatorDestinationNav3
            DemoNavigationEvent.Morph -> GraphNav3.BottomTab.Demo.MorphDemoDestinationNav3
            DemoNavigationEvent.HintPhoneNumber ->
                GraphNav3.BottomTab.Demo.HintPhoneDestinationNav3
            DemoNavigationEvent.Movie -> GraphNav3.BottomTab.Demo.MovieDestinationNav3
            DemoNavigationEvent.Dialog -> GraphNav3.BottomTab.Demo.DialogDestinationNav3
        }
    }
}
