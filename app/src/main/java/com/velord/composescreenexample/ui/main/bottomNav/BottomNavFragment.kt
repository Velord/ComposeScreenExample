package com.velord.composescreenexample.ui.main.bottomNav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.velord.composescreenexample.ui.compose.theme.setContentWithTheme
import com.velord.composescreenexample.utils.PermissionState
import com.velord.composescreenexample.utils.context.*
import com.velord.composescreenexample.utils.fragment.checkRecordVideoPermission
import com.velord.composescreenexample.utils.fragment.viewLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BottomNavFragment : Fragment() {

    private val viewModel by viewModels<BottomNavViewModel>()
    private val cameraRecordingViewModel by viewModels<CameraRecordingViewModel>()

    private val requestRecordVideoPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        val areGranted = it.values.reduce { acc, next -> acc && next }
        cameraRecordingViewModel.updatePermissionState(PermissionState.invoke(areGranted))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        BottomnNavScreen(viewModel)
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
                cameraRecordingViewModel.goToSettingsEvent.collect {
                    startActivity(requireContext().createSettingsIntent())
                }
            }
            launch {
                cameraRecordingViewModel.checkPermissionEvent.collect {
                    checkRecordVideoPermission()
                }
            }
        }
    }

    private fun checkRecordVideoPermission() {
        checkRecordVideoPermission(
            actionLauncher = requestRecordVideoPermissionLauncher,
            onGranted = {
                cameraRecordingViewModel.updatePermissionState(PermissionState.Granted)
            },
            onDecline = {
                cameraRecordingViewModel.updatePermissionState(PermissionState.Denied)
            }
        )
    }
}

@Composable
private fun BottomnNavScreen(
    viewModel: BottomNavViewModel,
) {

}
