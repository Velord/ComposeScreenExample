package com.velord.bottomnavigation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.velord.bottomnavigation.databinding.FragmentBottomNavBinding
import com.velord.multiplebackstackapplier.MultipleBackstack
import com.velord.multiplebackstackapplier.utils.compose.SnackBarOnBackPressHandler
import com.velord.resource.R
import com.velord.uicore.compose.component.AnimatableLabeledIcon
import com.velord.uicore.compose.preview.PreviewCombined
import com.velord.uicore.utils.setContentWithTheme
import com.velord.util.fragment.viewLifecycleScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "BottomNav"

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

class BottomNavFragment : Fragment(com.velord.bottomnavigation.R.layout.fragment_bottom_nav) {

    private val navController by lazy {
        childFragmentManager.fragments.first().findNavController()
    }
    private val viewModel by viewModel<BottomNavViewModelJetpack>()
    private var binding: FragmentBottomNavBinding? = null

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

        binding = FragmentBottomNavBinding.bind(view).apply {
            initView()
        }
        initObserving()
    }

    context(FragmentBottomNavBinding)
    private fun initView() {
        bottomNavBarView.setContentWithTheme {
            BottomNavScreen(viewModel)
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

@Composable
private fun BottomNavScreen(viewModel: BottomNavViewModelJetpack) {
    val tabFlow = viewModel.currentTabFlow.collectAsStateWithLifecycle()
    val backHandlingState = viewModel.backHandlingStateFlow.collectAsStateWithLifecycle()

    Content(
        selectedItem = tabFlow.value,
        onClick = viewModel::onTabClick,
    )

    Log.d(TAG, "BottomNavScreen: ${backHandlingState.value}")
    if (backHandlingState.value.isEnabled) {
        val str = stringResource(id = R.string.press_again_to_exit)
        SnackBarOnBackPressHandler(
            message = str,
            modifier = Modifier.padding(horizontal = 8.dp),
            enabled = backHandlingState.value.isEnabled,
            onBackClickLessThanDuration = viewModel::onBackDoubleClick,
        ) {
            Snackbar {
                Text(text = it.visuals.message)
            }
        }
    }
}

@Composable
private fun Content(
    selectedItem: BottomNavigationItem,
    onClick: (BottomNavigationItem) -> Unit,
) {
    NavigationBar {
        BottomNavigationItem.entries.forEach {
            val isSelected = selectedItem == it
            NavigationBarItem(
                selected = isSelected,
                onClick = { onClick(it) },
                label = {},
                icon = {
                    val color = MaterialTheme.colorScheme.run {
                        if (isSelected) secondary else onSurface
                    }
                    val painter = rememberVectorPainter(image = it.icon)
                    AnimatableLabeledIcon(
                        label = it.name,
                        painter = painter,
                        scale = if (isSelected) 1.5f else 1f,
                        color = color,
                        modifier = Modifier,
                        iconSize = 28.dp,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                        LocalAbsoluteTonalElevation.current
                    )
                )
            )
        }
    }
}

@PreviewCombined
@Composable
private fun BottomNavContentPreview() {
    Content(
        selectedItem = BottomNavigationItem.Camera,
        onClick = {},
    )
}
