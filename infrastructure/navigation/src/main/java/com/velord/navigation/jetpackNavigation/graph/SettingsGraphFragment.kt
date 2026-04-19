package com.velord.navigation.jetpackNavigation.graph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.velord.core.navigation.fragment.entryPoint.SettingsSourceFragment
import com.velord.core.ui.util.setContentWithTheme
import com.velord.navigation.R

class SettingsGraphFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = bundleOf(
            SettingsSourceFragment.ARGUMENT to SettingsSourceFragment.SettingsGraph,
        )
        this.findNavController().navigate(R.id.from_settingsGraphFragment_to_SettingsFragment, bundle)
    }
}
