package com.velord.composescreenexample.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.example.sharedviewmodel.ThemeViewModel
import com.velord.composescreenexample.R
import com.velord.composescreenexample.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val NAVIGATION_EXTRA = "navigation_extra"
        val fragmentContainer = R.id.navHostFragment

        fun startIntent(context: Context, bundle: Bundle) = Intent(
            context, MainActivity::class.java
        ).apply {
            putExtra(NAVIGATION_EXTRA, bundle)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
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

    private fun setNavGraph(
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