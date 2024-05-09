package com.velord.composescreenexample.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.NavHostFragment
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.navgraphs.MainNavGraph
import com.ramcosta.composedestinations.navigation.dependency
import com.velord.bottomnavigation.BottomNavScreen
import com.velord.composescreenexample.BuildConfig
import com.velord.composescreenexample.R
import com.velord.composescreenexample.databinding.ActivityMainBinding
import com.velord.composescreenexample.ui.main.navigation.NavigationLib
import com.velord.composescreenexample.ui.main.navigation.SupremeNavigator
import com.velord.sharedviewmodel.ThemeViewModel
import com.velord.splash.SplashScreen
import com.velord.splash.SplashViewModel
import com.velord.uicore.utils.setContentWithTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

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
    }

    private val viewModel: MainViewModel by viewModel()
    private val themeViewModel: ThemeViewModel by viewModel()
    private val splashViewModel: SplashViewModel by viewModel()
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

    private fun ComposeView.setContentAfterSplash(content: @Composable ComposeView.() -> Unit) {
        setContentWithTheme {
            SplashScreen(viewModel = splashViewModel) {
                content()
            }
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

                setContentAfterSplash {
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

                setContentAfterSplash {
                    val navController: NavHostController = rememberNavController()
                    DestinationsNavHost(
                        navGraph = MainNavGraph,
                        dependenciesContainerBuilder = {
                            dependency(SupremeNavigator(navController = navController))
                        }
                    )
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