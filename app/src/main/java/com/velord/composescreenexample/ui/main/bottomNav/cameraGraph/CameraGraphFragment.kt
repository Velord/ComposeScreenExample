package com.velord.composescreenexample.ui.main.bottomNav.cameraGraph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.velord.composescreenexample.R
import com.velord.composescreenexample.ui.compose.screen.TestScreen
import com.velord.composescreenexample.ui.compose.theme.setContentWithTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CameraGraphFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        TestScreen(R.string.open_new_camera) {
            findNavController().navigate(R.id.toCameraRecordingFragment)
        }
    }
}