package com.velord.composescreenexample.ui.main.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.NavHostAnimatedDestinationStyle
import com.ramcosta.composedestinations.annotation.ExternalDestination
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.annotation.parameters.CodeGenVisibility
import com.ramcosta.composedestinations.generated.modulebottomnavigation.destinations.BottomNavigationDestinationDestination
import com.ramcosta.composedestinations.generated.moduledemo.destinations.DemoDestinationDestination
import com.ramcosta.composedestinations.generated.modulefeaturecamerarecording.destinations.CameraRecordingDestinationDestination
import com.ramcosta.composedestinations.generated.modulefeaturesettings.destinations.SettingsBottomGraphDestinationDestination
import com.ramcosta.composedestinations.generated.modulefeaturesettings.destinations.SettingsCameraRecordingGraphDestinationDestination
import com.ramcosta.composedestinations.generated.navgraphs.BottomNavigationNavGraph
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavHostGraphSpec
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import com.velord.bottomnavigation.screen.BottomNavigationDestination
import com.velord.bottomnavigation.screen.BottomNavigator
import com.velord.camerarecording.CameraRecordingNavigator
import com.velord.feature.demo.DemoDest
import com.velord.feature.demo.DemoNavigator

object DefaultTransitions : NavHostAnimatedDestinationStyle() {

    override val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { 1000 },
            animationSpec = tween(700)
        )
    }

    override val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { -1000 },
            animationSpec = tween(700)
        )
    }

    override val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { -1000 },
            animationSpec = tween(700)
        )
    }

    override val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { 1000 },
            animationSpec = tween(700)
        )
    }
}

private const val MAIN_GRAPH = "main_nav_graph"
@NavHostGraph(
    defaultTransitions = DefaultTransitions::class,
    route = MAIN_GRAPH,
    visibility = CodeGenVisibility.INTERNAL
)
annotation class MainGraph {
    @ExternalDestination<BottomNavigationDestinationDestination>(start = true)
    companion object Includes
}

class SupremeNavigator(
    private val navController: NavHostController
) : BottomNavigator, DemoNavigator, CameraRecordingNavigator {

    override fun getRoute(route: BottomNavigationDestination): DestinationSpec = when(route) {
        BottomNavigationDestination.Camera -> CameraRecordingDestinationDestination
        BottomNavigationDestination.Demo -> DemoDestinationDestination
        BottomNavigationDestination.Settings -> SettingsBottomGraphDestinationDestination
    }

    override fun getGraph(): NavHostGraphSpec = BottomNavigationNavGraph

    @Composable
    override fun CreateDestinationsNavHostForBottom(
        navController: NavHostController,
        modifier: Modifier
    ) {
        DestinationsNavHost(
            navController = navController,
            navGraph = BottomNavigationNavGraph,
            modifier = modifier,
            dependenciesContainerBuilder = {
                dependency(SupremeNavigator(navController = navController))
            }
        )
    }

    override fun goTo(dest: DemoDest) {
        when(dest) {
            DemoDest.Shape -> {
                navController?.navigate("from_demoFragment_to_shapeDemoFragment")
            }

            DemoDest.Modifier -> {
                navController?.navigate("from_demoFragment_to_modifierDemoFragment")
            }

            DemoDest.FlowSummator -> {
                navController?.navigate("from_demoFragment_to_flowSummatorFragment")
            }

            DemoDest.Morph -> {
                navController?.navigate("from_demoFragment_to_morphDemoFragment")
            }
        }
    }

    override fun goToSettingsFromCameraRecording() {
        navController.toDestinationsNavigator().navigate(SettingsCameraRecordingGraphDestinationDestination)
    }
}

private const val BOTTOM_NAVIGATION_GRAPH = "bottom_navigation_graph"
@NavHostGraph(route = BOTTOM_NAVIGATION_GRAPH)
annotation class BottomNavigationGraph {
    @ExternalDestination<DemoDestinationDestination>()
    @ExternalDestination<SettingsBottomGraphDestinationDestination>()
    companion object Includes
}

private const val CAMERA_RECORDING_GRAPH = "camera_recording_graph"
@NavGraph<BottomNavigationGraph>(
    route = CAMERA_RECORDING_GRAPH,
    start = true
)
annotation class CameraRecordingGraph {
    @ExternalDestination<CameraRecordingDestinationDestination>(start = true)
    @ExternalDestination<SettingsBottomGraphDestinationDestination>()
    companion object Includes
}