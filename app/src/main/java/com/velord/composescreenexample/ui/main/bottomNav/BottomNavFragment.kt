package com.velord.composescreenexample.ui.main.bottomNav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.velord.composescreenexample.ui.compose.component.FullSizeBackground
import com.velord.composescreenexample.ui.compose.theme.RegularAmethystSmoke14Style
import com.velord.composescreenexample.ui.compose.theme.setContentWithTheme
import com.velord.composescreenexample.ui.compose.utils.getScreenWidthAndHeightInPx
import com.velord.composescreenexample.utils.PermissionState
import com.velord.composescreenexample.utils.context.createSettingsIntent
import com.velord.composescreenexample.utils.fragment.checkRecordVideoPermission
import com.velord.composescreenexample.utils.fragment.viewLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BottomNavFragment : Fragment() {

    private val navController by lazy {
        childFragmentManager.fragments.first().findNavController()
    }
    private val viewModel by viewModels<BottomNavViewModel>()
    private val recordVideoViewModel by viewModels<RecordVideoViewModel>()

    private val requestRecordVideoPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        val areGranted = it.values.reduce { acc, next -> acc && next }
        recordVideoViewModel.updatePermissionState(PermissionState.invoke(areGranted))
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        BottomNavScreen(viewModel, recordVideoViewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserving()
    }

    private fun initObserving() {
        viewLifecycleScope.launch {
            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    checkRecordVideoPermission()
                }
            }
            launch {
                recordVideoViewModel.goToSettingsEvent.collect {
                    startActivity(requireContext().createSettingsIntent())
                }
            }
            launch {
                recordVideoViewModel.checkPermissionEvent.collect {
                    checkRecordVideoPermission()
                }
            }
        }
    }

    private fun checkRecordVideoPermission() {
        checkRecordVideoPermission(
            actionLauncher = requestRecordVideoPermissionLauncher,
            onGranted = {
                recordVideoViewModel.updatePermissionState(PermissionState.Granted)
            },
            onDecline = {
                recordVideoViewModel.updatePermissionState(PermissionState.Denied)
            }
        )
    }
}

@Composable
private fun BottomNavScreen(
    viewModel: BottomNavViewModel,
    recordVideoViewModel: RecordVideoViewModel
) {
    val (screenWidthPx, screenHeightPx) = getScreenWidthAndHeightInPx()
    val url = "https://picsum.photos/seed/BottomNavScreen/$screenWidthPx/$screenHeightPx"
    FullSizeBackground(url = url)

    val permissionState = recordVideoViewModel.permissionFlow.collectAsStateWithLifecycle()

    Content(
        state = permissionState.value,
        onClick = recordVideoViewModel::onCheckPermission
    )
}

@Composable
private fun Content(
    state: PermissionState,
    onClick: () -> Unit
) {
    if (state.isDenied()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Text(
                text = "Check permission",
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
                    .background(Color.White)
                    .clickable { onClick() },
                style = RegularAmethystSmoke14Style
            )
        }
    }
}