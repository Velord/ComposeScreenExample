package com.velord.composescreenexample.ui.main.bottomNav.centerGraph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.velord.composescreenexample.R
import com.velord.composescreenexample.ui.main.demo.DemoViewModel
import com.velord.uicore.utils.setContentWithTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CenterGraphFragment : Fragment() {

    private val viewModel: CenterGraphViewModel by viewModels<CenterGraphViewModel>()

    private val viewModelDemo by viewModels<DemoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findNavController().navigate(R.id.from_addGraphFragment_to_demoFragment)

        viewModelDemo
    }
}