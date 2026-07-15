package com.velord.ui.feature.camerarecording

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import co.touchlab.kermit.Logger
import com.velord.core.resource.R
import com.velord.core.ui.dialog.checkRecordVideoPermission
import com.velord.core.ui.util.setContentWithTheme
import com.velord.infrastructure.util.fragment.viewLifecycleScope
import com.velord.infrastructure.util.permission.PermissionGrantState
import com.velord.ui.feature.bottomnavigation.screen.addTestCallback
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationJetpackUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationJetpackVM
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiAction
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingVM
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

private val log = Logger.withTag("CameraRecordingFragment")

class CameraRecordingFragment : Fragment() {

    private val viewModel by viewModel<CameraRecordingVM>()
    private val viewModelBottom by viewModel<BottomNavigationJetpackVM>()

    private val requestRecordVideoPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        log.d { "requestRecordVideoPermissionLauncher: $result" }
        val isCameraGranted = result.getOrDefault(
            key = android.Manifest.permission.CAMERA,
            defaultValue = false
        )
        val isAudioGranted = result.getOrDefault(
            key = android.Manifest.permission.RECORD_AUDIO,
            defaultValue = false
        )

        val cameraPermissionGrantState = PermissionGrantState.invoke(isCameraGranted)
        val cameraAction = CameraRecordingUiAction.UpdateCameraPermissionGrantState(
            cameraPermissionGrantState,
        )
        viewModel.onAction(cameraAction)

        val audioPermissionGrantState = PermissionGrantState.invoke(isAudioGranted)
        val audioAction = CameraRecordingUiAction.UpdateAudioPermissionGrantState(
            audioPermissionGrantState,
        )
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
        CameraRecordingScreen(
            viewModel = viewModel,
            needToHandlePermission = false,
            onNavigationEvent = ::handleNavigationEvent,
            onBackClick = {
                viewModelBottom.onAction(BottomNavigationJetpackUiAction.GraphCompletedHandling)
            },
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserving()
    }

    private fun initObserving() {
        viewLifecycleScope.launch {
            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    checkRecordVideoPermission()
                }
            }
            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.checkPermissionEvent.collect {
                        log.d { "checkPermissionEvent: $it" }
                        checkRecordVideoPermission()
                    }
                }
            }
        }
    }

    private fun handleNavigationEvent(event: CameraRecordingNavigationEvent) {
        when (event) {
            CameraRecordingNavigationEvent.Setting -> findNavController().navigate(
                R.id.from_cameraRecordingFragment_to_settingsFragment,
            )
        }
    }

    private fun checkRecordVideoPermission() {
        checkRecordVideoPermission(
            actionLauncher = requestRecordVideoPermissionLauncher,
            onGranted = {
                val action = CameraRecordingUiAction.UpdatePermissionGrantState(
                    PermissionGrantState.Granted,
                )
                viewModel.onAction(action)
            },
            onDecline = {
                val action = CameraRecordingUiAction.UpdatePermissionGrantState(
                    PermissionGrantState.Denied,
                )
                viewModel.onAction(action)
            },
        )
    }
}
