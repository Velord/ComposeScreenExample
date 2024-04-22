package com.velord.bottomnavigation

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.generated.destinations.CameraScreenDestination
import com.ramcosta.composedestinations.generated.destinations.DemoScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlin.random.Random

private const val BOTTOM_NAVIGATION_GRAPH = "bottom_navigation_graph"
@NavHostGraph(
    route = BOTTOM_NAVIGATION_GRAPH,
)
internal annotation class BottomNavigationGraph

@Destination<BottomNavigationGraph>(start = true)
@Composable
fun SettingsScreen(
    navigator: DestinationsNavigator,
    screenNumber: Int = 0,
) {
    val digit = remember {
        Random.nextInt(0, 100)
    }
    TestStackScreen(number = screenNumber, title = "Settings") {
        navigator.navigate(SettingsScreenDestination(digit))
    }
}

@Destination<BottomNavigationGraph>
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

@Destination<BottomNavigationGraph>()
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