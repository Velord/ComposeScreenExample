package com.velord.composescreenexample.ui.main.bottomNav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.velord.composescreenexample.R
import com.velord.navigation.entryPoint.SETTINGS_SOURCE
import com.velord.navigation.entryPoint.SettingsSource
import com.velord.uicore.utils.setContentWithTheme

class SettingsGraphFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = bundleOf(SETTINGS_SOURCE to SettingsSource.SettingsGraph)
        view.findNavController().navigate(R.id.from_settingsGraphFragment_to_SettingsFragment, bundle)
    }
}