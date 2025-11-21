package com.velord.navigation.compose.nav3.navigator

import android.util.Log
import com.velord.camerarecording.CameraRecordingNavigator
import com.velord.feature.demo.DemoDestinationNavigationEvent
import com.velord.feature.demo.DemoNavigator
import com.velord.navigation.compose.nav3.GraphNav3

internal class BottomNavigatorNav3(
    private val parent: SupremeNavigatorNav3,
    private val backStackNavigator: BackStackNavigator
) : DemoNavigator, CameraRecordingNavigator by parent {

    init {
        Log.d("LogBackStack - BottomNavigatorNav3", "init: ${this.backStackNavigator}")
    }

    override fun goTo(dest: DemoDestinationNavigationEvent) {
        val dest = when(dest) {
            DemoDestinationNavigationEvent.Shape -> GraphNav3.BottomTab.Demo.ShapeDemoDestinationNav3
            DemoDestinationNavigationEvent.Modifier -> GraphNav3.BottomTab.Demo.ModifierDestinationNav3
            DemoDestinationNavigationEvent.FlowSummator -> GraphNav3.BottomTab.Demo.FlowSummatorDestinationNav3
            DemoDestinationNavigationEvent.Morph -> GraphNav3.BottomTab.Demo.MorphDemoDestinationNav3
            DemoDestinationNavigationEvent.HintPhoneNumber -> GraphNav3.BottomTab.Demo.HintPhoneDestinationNav3
            DemoDestinationNavigationEvent.Movie -> GraphNav3.BottomTab.Demo.MovieDestinationNav3
            DemoDestinationNavigationEvent.Dialog -> GraphNav3.BottomTab.Demo.DialogDestinationNav3
        }
        backStackNavigator.navigate(dest)
    }
}