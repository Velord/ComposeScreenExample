package com.velord.composescreenexample.ui.main.bottomNav.settingsGraph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.velord.composescreenexample.R
import com.velord.composescreenexample.ui.compose.screen.TestScreen
import com.velord.uicore.utils.setContentWithTheme
import dagger.hilt.android.AndroidEntryPoint
import com.velord.uicore.R as RString

@AndroidEntryPoint
class SettingsGraphFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        TestScreen(RString.string.open_new_settings) {
            findNavController().navigate(R.id.toInDevelopmentFragment)
        }
    }
}