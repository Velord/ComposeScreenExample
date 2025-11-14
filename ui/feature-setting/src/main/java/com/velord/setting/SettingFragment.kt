package com.velord.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.velord.bottomnavigation.screen.jetpack.addTestCallback
import com.velord.bottomnavigation.viewmodel.BottomNavigationJetpackVM
import com.velord.core.ui.utils.setContentWithTheme
import com.velord.navigation.fragment.entryPoint.SETTINGS_SOURCE
import com.velord.navigation.fragment.entryPoint.SettingsSourceFragment
import com.velord.sharedviewmodel.ThemeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : Fragment() {

    private val viewModel by activityViewModels<ThemeViewModel>()
    private val viewModelBottom by viewModel<BottomNavigationJetpackVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        SettingScreen(viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isActivated = this.arguments?.get(SETTINGS_SOURCE) == SettingsSourceFragment.SettingsGraph
        if (isActivated.not()) return
        addTestCallback("Setting graph", viewModelBottom)
    }
}