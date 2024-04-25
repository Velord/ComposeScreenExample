package com.velord.composescreenexample.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.fragment.NavHostFragment
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.NavHostAnimatedDestinationStyle
import com.ramcosta.composedestinations.annotation.ExternalDestination
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.annotation.parameters.CodeGenVisibility
import com.ramcosta.composedestinations.generated.destinations.BottomNavigationDestinationsScreenDestination
import com.ramcosta.composedestinations.generated.navgraphs.MainNavGraph
import com.velord.bottomnavigation.BottomNavScreen
import com.velord.camerarecording.CameraRecordingScreen
import com.velord.composescreenexample.BuildConfig
import com.velord.composescreenexample.R
import com.velord.composescreenexample.databinding.ActivityMainBinding
import com.velord.composescreenexample.ui.compose.screen.TestScreen
import com.velord.feature.demo.DemoScreen
import com.velord.flowsummator.FlowSummatorScreen
import com.velord.modifierdemo.ModifierDemoScreen
import com.velord.navigation.SharedScreen
import com.velord.settings.SettingsScreen
import com.velord.shapedemo.ShapeDemoScreen
import com.velord.sharedviewmodel.ThemeViewModel
import com.velord.uicore.utils.setContentWithTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

enum class NavigationLib {
    Voyager,
    Jetpack,
    Destinations
}

class MainActivity : AppCompatActivity() {

    companion object {
        private const val NAVIGATION_EXTRA = "navigation_extra"
        private val fragmentContainer = R.id.navHostFragment

        fun startIntent(context: Context, bundle: Bundle) = Intent(
            context, MainActivity::class.java
        ).apply {
            putExtra(NAVIGATION_EXTRA, bundle)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        internal val featureMainModule = screenModule {
            register<SharedScreen.Test> {
                TestScreen(it.title, it.modifier, it.onClick)
            }
        }

        internal val featureBottomNavigationModule = screenModule {
            register<SharedScreen.BottomNavigationTab.Camera> {
               CameraRecordingScreen
            }
            register<SharedScreen.BottomNavigationTab.Demo> {
                DemoScreen
            }
            register<SharedScreen.BottomNavigationTab.Settings> {
                SettingsScreen
            }
        }

        internal val featureDemoModule = screenModule {
            register<SharedScreen.Demo.Shape> {
                ShapeDemoScreen
            }
            register<SharedScreen.Demo.Modifier> {
                ModifierDemoScreen
            }
            register<SharedScreen.Demo.FlowSummator> {
                FlowSummatorScreen
            }
        }
    }

    private val viewModel: MainViewModel by viewModel<MainViewModel>()
    private val themeViewModel: ThemeViewModel by viewModel()
    private var binding: ActivityMainBinding? = null

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        handleIntent(savedInstanceState)
        initObserving()
    }

    private fun handleIntent(savedInstanceState: Bundle?) {
        /** The Intent provided by getIntent() (and its extras) always persist the same
         * as it has been provided to activity first time(even process death has occurred).
         * To get around this, we must check savedInstanceState.
         * It is always null at the first launch of Activity.
         * **/
        if (savedInstanceState != null) {
            setNavGraph()
            return
        }

        intent?.extras?.let { extra ->
            extra.getBundle(NAVIGATION_EXTRA)?.let {
                // TODO: handle extra bundle
            }
        } ?: run {
            setNavGraph()
        }
    }

    private fun setNavGraph() {
        when(BuildConfig.NAVIGATION_LIB) {
            NavigationLib.Voyager -> setNavGraphViaVoyager()
            NavigationLib.Jetpack -> setNavGraphViaJetpack()
            NavigationLib.Destinations -> setNavGraphViaComposeDestinations()
            else -> setNavGraphViaJetpack()
        }
    }

    private fun setNavGraphViaVoyager() {
        binding?.apply {
            navHostFragment.isVisible = false
            mainNavHost.apply {
                isVisible = true

                setContentWithTheme {
                    Navigator(BottomNavScreen) {
                        SlideTransition(it)
                    }
                }
            }
        }
    }

    private fun setNavGraphViaJetpack(
        @IdRes destination: Int? = null,
        bundle: Bundle? = bundleOf()
    ) {
        val navHostFragment =
            (supportFragmentManager.findFragmentById(fragmentContainer) as? NavHostFragment)
                ?: supportFragmentManager.fragments[0] as NavHostFragment

        val controller = navHostFragment.navController
        val graph = controller.navInflater.inflate(R.navigation.main_nav_graph)
        controller.graph = graph

        if (destination != null) {
            controller.navigate(destination, bundle)
        }
    }

    private fun setNavGraphViaComposeDestinations() {
        binding?.apply {
            navHostFragment.isVisible = false
            mainNavHost.apply {
                isVisible = true

                setContentWithTheme {
                    DestinationsNavHost(navGraph = MainNavGraph)
                }
            }
        }
    }

    private fun initObserving() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                themeViewModel.themeFlow.collect { theme ->
                    viewModel.updateTheme(theme?.config)
                }
            }
        }
    }
}

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