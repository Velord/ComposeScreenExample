package com.velord.navigation.jetpackNavigation.inDevelopment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.velord.core.resource.R
import com.velord.core.ui.utils.setContentWithTheme
import com.velord.navigation.TestScreen

class TestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        TestScreen(R.string.test)
    }
}