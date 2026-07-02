package com.velord.ui.feature.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.velord.core.ui.util.setContentWithTheme
import com.velord.infrastructure.util.fragment.viewLifecycleScope
import com.velord.ui.feature.bottomnavigation.screen.jetpack.addTestCallback
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationJetpackVM
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DemoFragment : Fragment() {

    private val viewModel by viewModel<DemoViewModel>()
    private val viewModelBottom by viewModel<BottomNavigationJetpackVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        DemoScreen(
            viewModel = viewModel,
            onNavigationEvent = { _ -> },
            onBackClick = {
                // To enable System Back Button handling
                // via Bottom Navigation -> comment the line below
                // bottomNavViewModel.graphCompletedHandling()
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addTestCallback("Demo graph", viewModelBottom)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserving()
    }

    private fun initObserving() {
        viewLifecycleScope.launch {
            viewModel.navigationEventJetpack.collect {
                findNavController().navigate(it.id)
            }
        }
    }
}
