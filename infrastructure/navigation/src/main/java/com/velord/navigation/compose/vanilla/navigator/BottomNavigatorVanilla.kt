package com.velord.navigation.compose.vanilla.navigator

import android.util.Log
import androidx.navigation.NavHostController
import com.velord.camerarecording.CameraRecordingNavigator
import com.velord.feature.demo.DemoDest
import com.velord.feature.demo.DemoNavigator
import com.velord.navigation.compose.vanilla.FlowSummatorDestinationVanilla
import com.velord.navigation.compose.vanilla.HintPhoneDestinationVanilla
import com.velord.navigation.compose.vanilla.ModifierDestinationVanilla
import com.velord.navigation.compose.vanilla.MorphDemoDestinationVanilla
import com.velord.navigation.compose.vanilla.MovieDestinationVanilla
import com.velord.navigation.compose.vanilla.ShapeDemoDestinationVanilla

internal class BottomNavigatorVanilla(
    private val parent: SupremeNavigatorVanilla,
    private val navController: NavHostController
) : DemoNavigator, CameraRecordingNavigator by parent {

    init {
        Log.d("LogBackStack - BottomNavigator", "init: ${this.navController}")
    }

    override fun goTo(dest: DemoDest) {
        val dest = when(dest) {
            DemoDest.Shape -> ShapeDemoDestinationVanilla
            DemoDest.Modifier -> ModifierDestinationVanilla
            DemoDest.FlowSummator -> FlowSummatorDestinationVanilla
            DemoDest.Morph -> MorphDemoDestinationVanilla
            DemoDest.HintPhoneNumber -> HintPhoneDestinationVanilla
            DemoDest.Movie -> MovieDestinationVanilla
        }
        navController.navigate(dest)
    }
}