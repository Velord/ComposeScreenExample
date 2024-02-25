package com.velord.composescreenexample.ui.main.bottomNav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.velord.bottomnavigation.BottomNavViewModelJetpack
import com.velord.composescreenexample.R
import com.velord.uicore.utils.setContentWithTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class DemoGraphFragment : Fragment() {

    private val viewModel by viewModel<BottomNavViewModelJetpack>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findNavController().navigate(R.id.from_demoGraphFragment_to_demoFragment)
    }
}