package com.velord.navigation.compose.vanilla

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

internal class SupremeVanillaNavigator : BottomNavigatorVanilla {

    @Composable
    override fun CreateDestinationsNavHostForBottomVanilla(
        navController: NavHostController,
        modifier: Modifier,
        startRoute: BottomNavigationItem,
    ) {
        NavHost(
            navController = navController,
            startDestination = DemoDestinationVanilla,
            modifier = modifier
        ) {
            composable<DemoDestinationVanilla> {
                val viewModel = koinViewModel<DemoViewModel>()

                DemoScreen(viewModel) {
                    val dest = when(it) {
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

            composable<SettingsDestinationVanilla> {
                val viewModel = koinViewModel<ThemeViewModel>()
                SettingsScreen(viewModel)
            }

            navigation<CameraRecordingGraphVanilla>(startDestination = CameraRecordingDestinationVanilla) {
                composable<CameraRecordingDestinationVanilla> {
                    val viewModel = koinViewModel<CameraRecordingViewModel>()

                    CameraRecordingScreen(viewModel, true) {
                        when(it) {
                            CameraRecordingNavigationEvent.SETTINGS -> {
                                navController.navigate(CameraRecordingSettingsDestinationVanilla)
                            }
                        }
                    }
                }

                composable<CameraRecordingSettingsDestinationVanilla> {
                    val viewModel = koinViewModel<ThemeViewModel>()
                    SettingsScreen(viewModel)
                }
            }

            composable<ShapeDemoDestinationVanilla> {
                ShapeDemoScreen()
            }

            composable<ModifierDestinationVanilla> {
                ModifierDemoScreen()
            }

            composable<FlowSummatorDestinationVanilla> {
                val viewModel = koinViewModel<FlowSummatorViewModel>()
                FlowSummatorScreen(viewModel)
            }

            composable<MorphDemoDestinationVanilla> {
                MorphDemoScreen()
            }

            composable<HintPhoneDestinationVanilla> {
                HintPhoneNumberScreen()
            }

            composable<MovieDestinationVanilla> {
                val viewModel = koinViewModel<MovieViewModel>()
                MovieScreen(viewModel)
            }
        }
    }

    override fun getRoute(route: BottomNavigationItem): Any = when(route) {
        BottomNavigationItem.Camera -> CameraRecordingGraphVanilla
        BottomNavigationItem.Demo -> DemoDestinationVanilla
        BottomNavigationItem.Settings -> SettingsDestinationVanilla
    }

    override fun getStartRoute(route: BottomNavigationItem): Any = when(route) {
        BottomNavigationItem.Camera -> CameraRecordingDestinationVanilla
        BottomNavigationItem.Demo -> DemoDestinationVanilla
        BottomNavigationItem.Settings -> SettingsDestinationVanilla
    }
}