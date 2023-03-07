package com.velord.composescreenexample.utils.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Settings
import androidx.core.view.forEach
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.google.android.material.navigation.NavigationBarMenu
import com.velord.composescreenexample.R
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

/** For example:
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:app="http://schemas.android.com/apk/res-auto"
android:id="@+id/camera_nav_graph" // this is your navigationGraphId
app:startDestination="@id/cameraGraphFragment">
...
...
</navigation>
 **/
interface MultipleBackstackGraphItem {
    // Id of navigation graph for multiple backstack.
    // Graph must be included in "main|desirable|bottom" navigation graph.
    val navigationGraphId: Int
}

object MultipleBackstackApplier {

    private const val GROUP = 0
    private const val CATEGORY_ORDER = 0

    // Create "fake" NavigationBarMenu. Then summon MenuItems to it.
    // Ids of MenuItems must be the same as navigation graph ids.
    @SuppressLint("RestrictedApi")
    private fun createNavigationBarMenu(
        context: Context,
        items: List<MultipleBackstackGraphItem>
    ): NavigationBarMenu = NavigationBarMenu(
        context, this.javaClass, items.size
    ).apply {
        items.forEach {
            add(GROUP, it.navigationGraphId, CATEGORY_ORDER, null)
        }
    }

    @SuppressLint("RestrictedApi")
    fun setupWithNavController(
        items: List<MultipleBackstackGraphItem>,
        navigationView: View,
        navController: NavController,
        flowOnSelect: MutableSharedFlow<BottomNavigationItem>
    ) {
        val menu = createNavigationBarMenu(navigationView.context, items)

        navigationView.findViewTreeLifecycleOwner()?.let {
            it.lifecycleScope.launch {
                it.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    flowOnSelect.collectLatest { navItem ->
                        val menuItem = menu.findItem(navItem.navigationGraphId)
                        androidx.navigation.ui.NavigationUI.onNavDestinationSelected(
                            menuItem,
                            navController
                        )
                    }
                }
            }
        }
        val weakReference = WeakReference(navigationView)
        navController.addOnDestinationChangedListener(
            object : NavController.OnDestinationChangedListener {
                override fun onDestinationChanged(
                    controller: NavController,
                    destination: NavDestination,
                    arguments: Bundle?
                ) {
                    val view = weakReference.get()
                    if (view == null) {
                        navController.removeOnDestinationChangedListener(this)
                        return
                    }
                    menu.forEach { item ->
                        if (destination.matchDestination(item.itemId)) {
                            item.isChecked = true
                        }
                    }
                }
            }
        )
    }

    // Copy from androidx.navigation.ui.NavigationUI. Cause it's internal.
    internal fun NavDestination.matchDestination(@IdRes destId: Int): Boolean =
        hierarchy.any { it.id == destId }
}