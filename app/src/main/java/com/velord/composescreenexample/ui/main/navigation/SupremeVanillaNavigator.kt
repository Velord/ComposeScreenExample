package com.velord.composescreenexample.ui.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.velord.bottomnavigation.screen.BottomNavigationItem
import com.velord.bottomnavigation.screen.BottomNavigatorVanilla
import com.velord.camerarecording.CameraRecordingNavigationEvent
import com.velord.camerarecording.CameraRecordingScreen
import com.velord.camerarecording.CameraRecordingViewModel
import com.velord.feature.demo.DemoDest
import com.velord.feature.demo.DemoScreen
import com.velord.feature.demo.DemoViewModel
import com.velord.feature.movie.MovieScreen
import com.velord.feature.movie.viewModel.MovieViewModel
import com.velord.flowsummator.FlowSummatorScreen
import com.velord.flowsummator.FlowSummatorViewModel
import com.velord.hintphonenumber.HintPhoneNumberScreen
import com.velord.modifierdemo.ModifierDemoScreen
import com.velord.morphdemo.MorphDemoScreen
import com.velord.settings.SettingsScreen
import com.velord.shapedemo.ShapeDemoScreen
import com.velord.sharedviewmodel.ThemeViewModel
import org.koin.androidx.compose.koinViewModel

class SupremeVanillaNavigator : BottomNavigatorVanilla {

    @Composable
    override fun CreateDestinationsNavHostForBottomVanilla(
        navController: NavHostController,
        modifier: Modifier,
        startRoute: BottomNavigationItem,
    ) {
        NavHost(
            navController = navController,
            startDestination = DemoEntryPoint,
            modifier = modifier
        ) {
            composable<DemoEntryPoint> {
                val viewModel = koinViewModel<DemoViewModel>()

                DemoScreen(viewModel) {
                    val dest = when(it) {
                        DemoDest.Shape -> ShapeDemoEntryPoint
                        DemoDest.Modifier -> ModifierDemoEntryPoint
                        DemoDest.FlowSummator -> FlowSummatorEntryPoint
                        DemoDest.Morph -> MorphDemoEntryPoint
                        DemoDest.HintPhoneNumber -> HintPhoneNumberEntryPoint
                        DemoDest.Movie -> MovieEntryPoint
                    }
                    navController.navigate(dest)
                }
            }

            composable<SettingsEntryPoint> {
                val viewModel = koinViewModel<ThemeViewModel>()
                SettingsScreen(viewModel)
            }

            navigation<CameraRecordingGraph>(startDestination = CameraRecordingEntryPoint) {
                composable<CameraRecordingEntryPoint> {
                    val viewModel = koinViewModel<CameraRecordingViewModel>()

                    CameraRecordingScreen(viewModel, true) {
                        when(it) {
                            CameraRecordingNavigationEvent.SETTINGS -> {
                                navController.navigate(CameraRecordingSettingsEntryPoint)
                            }
                        }
                    }
                }

                composable<CameraRecordingSettingsEntryPoint> {
                    val viewModel = koinViewModel<ThemeViewModel>()
                    SettingsScreen(viewModel)
                }
            }

            composable<ShapeDemoEntryPoint> {
                ShapeDemoScreen()
            }

            composable<ModifierDemoEntryPoint> {
                ModifierDemoScreen()
            }

            composable<FlowSummatorEntryPoint> {
                val viewModel = koinViewModel<FlowSummatorViewModel>()
                FlowSummatorScreen(viewModel)
            }

            composable<MorphDemoEntryPoint> {
                MorphDemoScreen()
            }

            composable<HintPhoneNumberEntryPoint> {
                HintPhoneNumberScreen()
            }

            composable<MovieEntryPoint> {
                val viewModel = koinViewModel<MovieViewModel>()
                MovieScreen(viewModel)
            }
        }
    }

    override fun getRoute(route: BottomNavigationItem): Any = when(route) {
        BottomNavigationItem.Camera -> CameraRecordingGraph
        BottomNavigationItem.Demo -> DemoEntryPoint
        BottomNavigationItem.Settings -> SettingsEntryPoint
    }

    override fun getStartRoute(route: BottomNavigationItem): Any = when(route) {
        BottomNavigationItem.Camera -> CameraRecordingEntryPoint
        BottomNavigationItem.Demo -> DemoEntryPoint
        BottomNavigationItem.Settings -> SettingsEntryPoint
    }
}