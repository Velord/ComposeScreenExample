package com.velord.composescreenexample.ui.main.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.ramcosta.composedestinations.animations.NavHostAnimatedDestinationStyle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.ExternalDestination
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.annotation.parameters.CodeGenVisibility
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.BottomNavigationDestinationsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.CameraScreenDestination
import com.ramcosta.composedestinations.generated.destinations.DemoScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavHostGraphSpec
import com.velord.bottomnavigation.screen.BottomNavigationDestination
import com.velord.bottomnavigation.screen.BottomNavigator
import kotlin.random.Random

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
    @ExternalDestination<BottomNavigationDestinationsScreenDestination>(start = true)
    companion object Includes
}

class SupremeNavigator(
    private val navController: NavController
) : BottomNavigator {

    override fun getRoute(route: BottomNavigationDestination): DestinationSpec = when(route) {
        BottomNavigationDestination.Camera -> CameraScreenDestination
        BottomNavigationDestination.Demo -> DemoScreenDestination
        BottomNavigationDestination.Settings -> DemoScreenDestination
    }

    override fun getGraph(): NavHostGraphSpec = NavGraphs.bottomNavigationGraph
}

private const val BOTTOM_NAVIGATION_GRAPH = "bottom_navigation_graph"
@NavHostGraph(route = BOTTOM_NAVIGATION_GRAPH)
annotation class BottomNavigationGraph

@Composable
private fun TestStackScreen(
    number: Int,
    title: String,
    onClick: () -> Unit
) {
    Button(onClick = onClick) {
        Text(text = "$title $number")
    }
}

@Destination<BottomNavigationGraph>(start = true)
@Composable
fun CameraScreen(
    navigator: DestinationsNavigator,
    screenNumber: Int = 0,
) {
    val digit = remember {
        Random.nextInt(0, 100)
    }
    TestStackScreen(number = screenNumber, title = "Camera") {
        navigator.navigate(CameraScreenDestination(digit))
    }
}

@Destination<BottomNavigationGraph>()
@Composable
fun DemoScreen(
    navigator: DestinationsNavigator,
    screenNumber: Int = 0,
) {
    val digit = remember {
        Random.nextInt(0, 100)
    }
    TestStackScreen(number = screenNumber, title = "Demo") {
        navigator.navigate(DemoScreenDestination(digit))
    }
}