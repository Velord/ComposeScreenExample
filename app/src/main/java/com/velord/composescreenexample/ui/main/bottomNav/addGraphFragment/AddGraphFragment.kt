package com.velord.composescreenexample.ui.main.bottomNav.addGraphFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.fragment.app.Fragment
import com.velord.composescreenexample.ui.compose.theme.setContentWithTheme
import com.velord.composescreenexample.ui.main.cameraRecording.CameraRecordingScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddGraphFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        CameraRecordingScreen(viewModel)
    }
}

@Composable
private fun