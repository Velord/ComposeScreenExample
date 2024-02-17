package com.velord.composescreenexample.ui.main.inDevelopment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.velord.composescreenexample.ui.compose.screen.TestScreen
import com.velord.resource.R
import com.velord.uicore.utils.setContentWithTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        TestScreen(R.string.test)
    }
}