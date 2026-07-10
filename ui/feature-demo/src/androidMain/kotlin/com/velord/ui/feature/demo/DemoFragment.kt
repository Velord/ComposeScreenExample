package com.velord.ui.feature.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.velord.core.resource.R
import com.velord.core.ui.util.setContentWithTheme
import com.velord.ui.feature.bottomnavigation.screen.jetpack.addTestCallback
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationJetpackVM
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "Demo graph"

class DemoFragment : Fragment() {

    private val viewModel by viewModel<DemoViewModel>()
    private val viewModelBottom by viewModel<BottomNavigationJetpackVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = setContentWithTheme {
        DemoScreen(
            viewModel = viewModel,
            onNavigationEvent = ::handleNavigationEvent,
            onBackClick = {
                // To enable System Back Button handling
                // via Bottom Navigation -> comment the line below
                // bottomNavViewModel.graphCompletedHandling()
            },
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addTestCallback(TAG, viewModelBottom)
    }

    private fun handleNavigationEvent(event: DemoNavigationEvent) {
        val destinationId = event.jetpackDestinationId ?: return
        findNavController().navigate(destinationId)
    }
}

private val DemoNavigationEvent.jetpackDestinationId: Int? get() = when (this) {
    DemoNavigationEvent.Shape -> R.id.from_demoFragment_to_shapeDemoFragment
    DemoNavigationEvent.Modifier -> R.id.from_demoFragment_to_modifierDemoFragment
    DemoNavigationEvent.FlowSummator -> R.id.from_demoFragment_to_flowSummatorFragment
    DemoNavigationEvent.Morph -> R.id.from_demoFragment_to_morphDemoFragment
    DemoNavigationEvent.HintPhoneNumber -> null
    DemoNavigationEvent.Movie -> null
    DemoNavigationEvent.Dialog -> null
}
