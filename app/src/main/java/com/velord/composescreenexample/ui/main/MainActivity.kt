package com.velord.composescreenexample.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScreenTransition
import cafe.adriel.voyager.transitions.ScreenTransitionContent
import com.example.sharedviewmodel.ThemeViewModel
import com.velord.bottomnavigation.BottomNavScreen
import com.velord.camerarecording.CameraRecordingScreen
import com.velord.composescreenexample.databinding.ActivityMainBinding
import com.velord.composescreenexample.ui.compose.screen.TestScreen
import com.velord.feature.demo.DemoScreen
import com.velord.flowsummator.FlowSummatorScreen
import com.velord.modifierdemo.ModifierDemoScreen
import com.velord.navigation.SharedScreen
import com.velord.settings.SettingsScreen
import com.velord.shapedemo.ShapeDemoScreen
import com.velord.uicore.utils.setContentWithTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        private const val NAVIGATION_EXTRA = "navigation_extra"

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

    private val viewModel: MainViewModel by viewModels()
    private val themeViewModel: ThemeViewModel by viewModels()
    private var binding: ActivityMainBinding? = null

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
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
        binding?.mainNavHost?.setContentWithTheme {
            Navigator(BottomNavScreen) {
                Transition(it)
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

@Composable
fun Transition(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    content: ScreenTransitionContent = { it.Content() }
) {
    val transition: AnimatedContentTransitionScope<Screen>.() -> ContentTransform = {
        when {
            navigator.lastEvent == StackEvent.Pop && navigator.lastItem == BottomNavScreen -> {
                scaleIn(
                    initialScale = 0.1f,
                ) togetherWith scaleOut(
                    targetScale = 0.2f
                )
            }
            else -> {
                val animationSpec: FiniteAnimationSpec<IntOffset> = tween(
                    durationMillis = 500,
                    delayMillis = 100,
                    easing = LinearEasing
                )
                val (initialOffset, targetOffset) = when (navigator.lastEvent) {
                    StackEvent.Pop -> ({ size: Int -> -size }) to ({ size: Int -> size })
                    else -> ({ size: Int -> size }) to ({ size: Int -> -size })
                }
                slideInHorizontally(animationSpec, initialOffset) togetherWith
                        slideOutHorizontally(animationSpec, targetOffset)
            }
        }
    }
    ScreenTransition(
        navigator = navigator,
        transition = transition,
        modifier = modifier,
        content = content,
    )
}