package com.velord.composescreenexample.ui.main.bottomNav

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.velord.composescreenexample.R
import com.velord.composescreenexample.databinding.FragmentBottomNavBinding
import com.velord.composescreenexample.ui.compose.component.AnimatableLabeledIcon
import com.velord.composescreenexample.ui.compose.preview.PreviewCombined
import com.velord.composescreenexample.ui.compose.theme.setContentWithTheme
import com.velord.composescreenexample.utils.fragment.viewLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BottomNavFragment : Fragment(R.layout.fragment_bottom_nav) {

    private val navController by lazy {
        childFragmentManager.fragments.first().findNavController()
    }
    private val viewModel by viewModels<BottomNavViewModel>()
    private var binding: FragmentBottomNavBinding? = null

    override fun onDestroy() {
        binding = null
        super.onDestroy()
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
                viewModel.tabFlow.collectLatest {
                    navController.navigate(it.navigationId)
                }
            }
        }
    }
}

@Composable
private fun BottomNavScreen(viewModel: BottomNavViewModel) {
    val tabFlow = viewModel.tabFlow.collectAsStateWithLifecycle()

    Content(
        selectedItem = tabFlow.value,
        onClick = viewModel::onTabClick,
    )
}

@Composable
private fun Content(
    selectedItem: BottomNavItem,
    onClick: (BottomNavItem) -> Unit,
) {
    NavigationBar(
        modifier = Modifier
            .navigationBarsPadding()
            .height(72.dp),
    ) {
        BottomNavItem.values().forEach {
            val isSelected = selectedItem == it
            NavigationBarItem(
                selected = isSelected,
                onClick = { onClick(it) },
                label = {},
                icon = {
                    val color = MaterialTheme.colorScheme.run {
                        if (isSelected) secondary else onSurface
                    }
                    AnimatableLabeledIcon(
                        label = it.name,
                        imageVector = it.icon,
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
        selectedItem = BottomNavItem.Camera,
        onClick = {},
    )
}
