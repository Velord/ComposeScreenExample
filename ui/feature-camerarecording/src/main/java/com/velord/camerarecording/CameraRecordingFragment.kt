package com.velord.camerarecording

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.velord.bottomnavigation.screen.jetpack.addTestCallback
import com.velord.bottomnavigation.viewmodel.BottomNavigationJetpackVM
import com.velord.uicore.dialog.checkRecordVideoPermission
import com.velord.uicore.utils.setContentWithTheme
import com.velord.util.fragment.viewLifecycleScope
import com.velord.util.permission.AndroidPermissionState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class CameraRecordingFragment : Fragment() {

    private val viewModel by viewModel<CameraRecordingViewModel>()
    private val viewModelBottom by viewModel<BottomNavigationJetpackVM>()

    private val requestRecordVideoPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        Log.d("CameraRecordingFragment", "requestRecordVideoPermissionLauncher: $result")
        val isCameraGranted = result.getOrDefault(
            key = android.Manifest.permission.CAMERA,
            defaultValue = false
        )
        val isAudioGranted = result.getOrDefault(
            key = android.Manifest.permission.RECORD_AUDIO,
            defaultValue = false
        )

        val cameraPermissionState = AndroidPermissionState.invoke(isCameraGranted)
        val cameraAction = CameraRecordingUiAction.UpdateCameraPermissionState(cameraPermissionState)
        viewModel.onAction(cameraAction)

        val audioPermissionState = AndroidPermissionState.invoke(isAudioGranted)
        val audioAction = CameraRecordingUiAction.UpdateAudioPermissionState(audioPermissionState)
        viewModel.onAction(audioAction)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addTestCallback("Camera graph", viewModelBottom)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        CameraRecordingScreen(viewModel) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserving()
    }

    private fun initObserving() {
        viewLifecycleScope.launch {
            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    Log.d("CameraRecordingFragment", "Lifecycle.State.STARTED")
                    checkRecordVideoPermission()
                }
            }
            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.checkPermissionEvent.collect {
                        Log.d("CameraRecordingFragment", "checkPermissionEvent: $it")
                        checkRecordVideoPermission()
                    }
                }
            }
            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.navigationEventJetpack.filterNotNull().collect {
                        findNavController().navigate(it.id, it.bundle)
                    }
                }
            }
        }
    }

    private fun checkRecordVideoPermission() {
        checkRecordVideoPermission(
            actionLauncher = requestRecordVideoPermissionLauncher,
            onGranted = {
                val action = CameraRecordingUiAction.UpdatePermissionState(AndroidPermissionState.Granted)
                viewModel.onAction(action)
            },
            onDecline = {
                val action = CameraRecordingUiAction.UpdatePermissionState(AndroidPermissionState.Denied)
                viewModel.onAction(action)
            }
        )
    }
}