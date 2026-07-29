package com.velord.infrastructure.navigation

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.compose.runtime.Composable
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.velord.infrastructure.config.NavigationLib

class JetpackParameter(
    val fragmentManager: FragmentManager,
    @IdRes val fragmentContainerId: Int,
    val destination: Int?,
    val bundle: Bundle?,
)

class ContainerParameter(val composeView: View, val fragmentView: View)

object AndroidNavigationHost {

    fun resolveNavigation(
        navigationLib: NavigationLib,
        containerParameter: ContainerParameter,
        jetpackParameter: JetpackParameter,
        installComposeContent: (@Composable () -> Unit) -> Unit,
    ) {
        if (navigationLib.isJetpack) {
            containerParameter.composeView.isVisible = false
            containerParameter.fragmentView.isVisible = true
            setNavGraphViaJetpack(jetpackParameter)
            return
        }

        containerParameter.composeView.isVisible = true
        containerParameter.fragmentView.isVisible = false
        installComposeContent {
            NavigationHost(navigationLib)
        }
    }

    private fun setNavGraphViaJetpack(parameter: JetpackParameter) {
        val navHostFragment = parameter.fragmentManager.findNavHostFragment(parameter.fragmentContainerId)
        val controller = navHostFragment.navController
        val graph = controller.navInflater.inflate(R.navigation.main_nav_graph)
        controller.graph = graph

        if (parameter.destination != null) {
            controller.navigate(parameter.destination, parameter.bundle)
        }
    }

    private fun FragmentManager.findNavHostFragment(
        @IdRes fragmentContainerId: Int,
    ): NavHostFragment {
        val navHostFragment = findFragmentById(fragmentContainerId) as? NavHostFragment
            ?: fragments.filterIsInstance<NavHostFragment>().firstOrNull()

        return navHostFragment ?: error("NavHostFragment not found")
    }
}
