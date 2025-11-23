package com.velord.flowsummator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.velord.core.ui.utils.setContentWithTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class FlowSummatorFragment : Fragment() {

    private val viewModel by viewModel<FlowSummatorViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        FlowSummatorScreen(viewModel)
    }
}