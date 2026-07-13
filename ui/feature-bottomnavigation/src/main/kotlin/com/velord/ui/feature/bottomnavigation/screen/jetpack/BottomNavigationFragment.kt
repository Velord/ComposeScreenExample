package com.velord.ui.feature.bottomnavigation.screen.jetpack

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import co.touchlab.kermit.Logger
import com.velord.core.ui.util.setContentWithTheme
import com.velord.infrastructure.util.fragment.viewLifecycleScope
import com.velord.multiplebackstackapplier.MultipleBackstack
import com.velord.ui.feature.bottomnavigation.R
import com.velord.ui.feature.bottomnavigation.databinding.FragmentBottomNavigationBinding
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationJetpackUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationJetpackVM
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

internal const val TAG = "BottomNav"
private val log = Logger.withTag(TAG)

fun Fragment.addTestCallback(
    tag: String,
    viewModel: BottomNavigationJetpackVM
) {
    // Android 13+
    // With fragments does not work March 2024
//    requireActivity().onBackInvokedDispatcher.registerOnBackInvokedCallback(
//        OnBackInvokedDispatcher.PRIORITY_DEFAULT
//    ) {
//        requireContext().fireToast(tag)
//        viewModel.graphCompletedHandling()
//    }
    requireActivity().onBackPressedDispatcher.addCallback(
        this,
        true
    ) {
        viewModel.onAction(BottomNavigationJetpackUiAction.ShowBackPressToast(tag))
        isEnabled = false
        viewModel.onAction(BottomNavigationJetpackUiAction.GraphCompletedHandling)
        log.d { "onBackPressedDispatcher" }
    }
}

class BottomNavigationFragment : Fragment(R.layout.fragment_bottom_navigation) {

    private val navController by lazy {
        childFragmentManager.fragments.first().findNavController()
    }
    private val viewModel by viewModel<BottomNavigationJetpackVM>()
    private var binding: FragmentBottomNavigationBinding? = null

    private val multipleBackStack by lazy {
        MultipleBackstack(
            navController = lazy { navController },
            lifecycleOwner = this,
            context = requireContext(),
            items = BottomNavigationItem.entries.map { item ->
                item.toMultipleBackstackGraphItem()
            },
            flowOnSelect = viewModel.uiStateFlow.map { state ->
                state.currentTab.toMultipleBackstackGraphItem()
            },
            onMenuChange = {
                val current = navController.currentDestination
                viewModel.onAction(BottomNavigationJetpackUiAction.UpdateBackHandling(current))
            }
        )
    }

    override fun onDestroy() {
        binding = null
        lifecycle.removeObserver(multipleBackStack)
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(multipleBackStack)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentBottomNavigationBinding.bind(view).apply {
            initView()
        }
        initObserving()
    }

    context(b: FragmentBottomNavigationBinding)
    private fun initView() {
        b.bottomNavBarView.setContentWithTheme {
            BottomNavigationJetpackScreen(viewModel)
        }
    }

    private fun initObserving() {
        viewLifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.finishAppEvent.collect {
                    requireActivity().finish()
                }
            }
        }
    }
}
