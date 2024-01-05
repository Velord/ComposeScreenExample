package com.velord.composescreenexample.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.navigator.Navigator
import com.example.sharedviewmodel.ThemeViewModel
import com.velord.bottomnavigation.BottomNavScreen
import com.velord.composescreenexample.databinding.ActivityMainBinding
import com.velord.navigation.SharedScreen
import com.velord.settings.SettingsScreen
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

        internal val featureBottomNavigationModule = screenModule {
            // TODO: uncomment when you need to use this screen
//            register<SharedScreen.BottomNavigationTab.Camera> {
//                BottomNavigationTab.Camera
//            }
//            register<SharedScreen.BottomNavigationTab.Demo> {
//                BottomNavigationTab.Demo
//            }
            register<SharedScreen.BottomNavigationTab.Settings> {
                SettingsScreen()
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
            Navigator(BottomNavScreen)
        }
    }

    private fun initObserving() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                themeViewModel.themeFlow.collect { theme ->
                    Log.d("@@@", "MainActivity theme: ${theme?.config}")
                    viewModel.updateTheme(theme?.config)
                }
            }
        }
    }
}