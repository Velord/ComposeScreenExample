package com.velord.bottomnavigation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.velord.bottomnavigation.databinding.FragmentBottomNavigationBinding
import com.velord.bottomnavigation.screen.BottomNavigationJetpackScreen
import com.velord.bottomnavigation.viewmodel.BottomNavViewModelJetpack
import com.velord.multiplebackstackapplier.MultipleBackstack
import com.velord.uicore.utils.setContentWithTheme
import com.velord.util.fragment.viewLifecycleScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

internal const val TAG = "BottomNav"

private fun Context.fireToast(text: String) {
    val description = "I am the first at $text"
    Toast.makeText(this, description, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        show()
    }
}

fun Fragment.addTestCallback(
    tag: String,
    viewModel: BottomNavViewModelJetpack
) {
    // Android 13+
    // With fragments does not work March 2024
//    requireActivity().onBackInvokedDispatcher.registerOnBackInvokedCallback(
//        OnBackInvokedDispatcher.PRIORITY_DEFAULT
//    ) {
//        requireContext().fireToast(tag)
//        viewModel.graphCompletedHandling()
//        Log.d(TAG, "onBackPressedDispatcher")
//    }
    requireActivity().onBackPressedDispatcher.addCallback(
        this,
        true
    ) {
        requireContext().fireToast(tag)
        isEnabled = false
        viewModel.graphCompletedHandling()
        Log.d(TAG, "onBackPressedDispatcher")
    }
}

class BottomNavigationFragment : Fragment(com.velord.bottomnavigation.R.layout.fragment_bottom_navigation) {

    private val navController by lazy {
        childFragmentManager.fragments.first().findNavController()
    }
    private val viewModel by viewModel<BottomNavViewModelJetpack>()
    private var binding: FragmentBottomNavigationBinding? = null

    private val multipleBackStack by lazy {
        MultipleBackstack(
            navController = lazy { navController },
            lifecycleOwner = this,
            context = requireContext(),
            items = viewModel.getNavigationItems(),
            flowOnSelect = viewModel.currentTabFlow,
            onMenuChange = {
                val current = navController.currentDestination
                viewModel.updateBackHandling(current)
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

    context(FragmentBottomNavigationBinding)
    private fun initView() {
        bottomNavBarView.setContentWithTheme {
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
