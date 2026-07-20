package com.velord.composescreenexample.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.velord.composescreenexample.R
import com.velord.composescreenexample.databinding.ActivityMainBinding
import com.velord.core.ui.compose.glance.GlanceWidgetThemeSustainer
import com.velord.core.ui.compose.glance.updateAll
import com.velord.core.ui.util.setContentWithTheme
import com.velord.core.ui.util.setToastOverlayWithTheme
import com.velord.infrastructure.config.NavigationLib
import com.velord.infrastructure.navigation.CreateNavigationViaDestinations
import com.velord.infrastructure.navigation.CreateNavigationViaNav3
import com.velord.infrastructure.navigation.CreateNavigationViaVanilla
import com.velord.infrastructure.navigation.CreateNavigationViaVoyager
import com.velord.model.AppEvent
import com.velord.ui.feature.splash.SplashScreen
import com.velord.ui.feature.splash.SplashVM
import com.velord.ui.feature.splash.installSplash
import com.velord.ui.sharedviewmodel.MainVM
import com.velord.ui.sharedviewmodel.ThemeVM
import com.velord.ui.widget.counter.CounterWidget
import com.velord.ui.widget.refreshableimage.RefreshableImageWidget
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.velord.infrastructure.navigation.R as RNavigation

class MainActivity : AppCompatActivity() {

    companion object {
        private const val NAVIGATION_EXTRA = "navigation_extra"
        private val fragmentContainer = R.id.navHostFragment

        private val widgetRoster = listOf<GlanceWidgetThemeSustainer<*>>(
            RefreshableImageWidget(), CounterWidget()
        )

        fun startIntent(context: Context, bundle: Bundle) = Intent(
            context,
            MainActivity::class.java,
        ).apply {
            putExtra(NAVIGATION_EXTRA, bundle)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
    }

    private val mainVM: MainVM by viewModel()
    private val themeVM: ThemeVM by viewModel()
    private val splashVM: SplashVM by viewModel()
//    Activity root
//    ├─ mainNavHost          // Compose navigation(Voyager, Vanilla, Destinations, Nav3)
//    ├─ navHostFragment      // Jetpack navigation
//    └─ toastOverlay         // Global toast, always above both
    private var binding: ActivityMainBinding? = null

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplash()

        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding?.apply {
            setContent()
            handleIntent(savedInstanceState)
        }
        initObserving()
    }

    context(b: ActivityMainBinding)
    private fun setContent() {
        b.apply {
            setContentView(root)
            toastOverlay.setToastOverlayWithTheme(toastEventFlow = mainVM.toastConfigFlow)
        }
    }

    context(b: ActivityMainBinding)
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
            SplashScreen(viewModel = splashVM) {
                content()
            }
        }
    }

    context(b: ActivityMainBinding)
    private fun setNavGraph() {
        when (mainVM.navigationLib) {
            NavigationLib.Voyager -> setNavGraphViaVoyager()
            NavigationLib.Jetpack -> setNavGraphViaJetpack()
            NavigationLib.Destinations -> setNavGraphViaComposeDestinations()
            NavigationLib.Compose -> setNavGraphViaCompose()
            NavigationLib.Nav3 -> setNavGraphViaNav3()
        }
    }

    context(b: ActivityMainBinding)
    private fun setNavGraphViaVoyager() {
        b.apply {
            navHostFragment.isVisible = false
            mainNavHost.apply {
                isVisible = true

                setContentAfterSplash {
                    CreateNavigationViaVoyager()
                }
            }
        }
    }

    context(b: ActivityMainBinding)
    private fun setNavGraphViaJetpack(
        @IdRes destination: Int? = null,
        bundle: Bundle? = bundleOf()
    ) {
        b.apply {
            mainNavHost.isVisible = false
            navHostFragment.isVisible = true
        }

        val navHostFragment =
            (supportFragmentManager.findFragmentById(fragmentContainer) as? NavHostFragment)
                ?: supportFragmentManager.fragments[0] as NavHostFragment

        val controller = navHostFragment.navController
        val graph = controller.navInflater.inflate(RNavigation.navigation.main_nav_graph)
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
                    CreateNavigationViaDestinations()
                }
            }
        }
    }

    private fun setNavGraphViaCompose() {
        binding?.apply {
            navHostFragment.isVisible = false
            mainNavHost.apply {
                isVisible = true

                setContentAfterSplash {
                    CreateNavigationViaVanilla()
                }
            }
        }
    }

    private fun setNavGraphViaNav3() {
        binding?.apply {
            navHostFragment.isVisible = false
            mainNavHost.apply {
                isVisible = true

                setContentAfterSplash {
                    CreateNavigationViaNav3()
                }
            }
        }
    }

    private fun initObserving() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                themeVM.uiStateFlow.mapNotNull { it.appThemeConfig?.config }.collect { theme ->
                    widgetRoster.updateAll(this@MainActivity, theme)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                mainVM.appEventFlow.collect { event ->
                    when (event) {
                        AppEvent.Exit -> finish()
                        is AppEvent.Toast -> Unit
                    }
                }
            }
        }
    }
}
