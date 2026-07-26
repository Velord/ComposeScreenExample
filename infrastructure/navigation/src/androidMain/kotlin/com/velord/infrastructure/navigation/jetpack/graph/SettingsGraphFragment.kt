package com.velord.infrastructure.navigation.jetpack.graph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.velord.core.navigation.fragment.entryPoint.SettingsSourceFragment
import com.velord.core.ui.util.setContentWithTheme
import com.velord.infrastructure.navigation.R
import com.velord.core.resource.R as CoreResourceR

class SettingsGraphFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        val currentDestinationId = navController.currentDestination?.id
        val graphDestinationId = CoreResourceR.id.bottom_nav_graph_right_start_destination
        if (currentDestinationId != graphDestinationId) return

        val bundle = bundleOf(
            SettingsSourceFragment.ARGUMENT to SettingsSourceFragment.SettingsGraph,
        )
        navController.navigate(
            resId = R.id.from_settingsGraphFragment_to_SettingsFragment,
            args = bundle
        )
    }
}
