package com.velord.ui.feature.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.velord.core.navigation.fragment.NAVIGATION_PAYLOAD
import com.velord.core.navigation.fragment.entryPoint.SettingsSourceFragment
import com.velord.core.ui.util.setContentWithTheme
import com.velord.ui.feature.bottomnavigation.screen.jetpack.addTestCallback
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationJetpackVM
import com.velord.ui.sharedviewmodel.ThemeVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : Fragment() {

    private val viewModel by activityViewModels<ThemeVM>()
    private val viewModelBottom by viewModel<BottomNavigationJetpackVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        SettingScreen(viewModel) {
            viewModelBottom.graphCompletedHandling()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sourceFromPayload = arguments
            ?.getString(NAVIGATION_PAYLOAD)
            ?.let(SettingsSourceFragment::decode)
        val sourceFromArgs = arguments
            ?.get(SettingsSourceFragment.ARGUMENT) as? SettingsSourceFragment
        val source = sourceFromPayload ?: sourceFromArgs
        val isActivated = source == SettingsSourceFragment.SettingsGraph
        if (isActivated.not()) return
        addTestCallback("Setting graph", viewModelBottom)
    }
}
