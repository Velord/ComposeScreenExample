package com.velord.composescreenexample.ui.main.bottomNav.cameraGraph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.velord.composescreenexample.ui.compose.screen.TestScreen
import com.velord.resource.R
import com.velord.uicore.utils.setContentWithTheme
import dagger.hilt.android.AndroidEntryPoint
import com.velord.composescreenexample.R as RNav

@AndroidEntryPoint
class CameraGraphFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        TestScreen(R.string.open_new_camera) {
            findNavController().navigate(RNav.id.toCameraRecordingFragment)
        }
    }
}