package com.velord.composescreenexample.ui.main.bottomNav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.velord.composescreenexample.ui.compose.theme.setContentWithTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomNavFragment : Fragment() {

    private val navController by lazy {
        childFragmentManager.fragments.first().findNavController()
    }
    private val viewModel by viewModels<BottomNavViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        AuthViaExternalServiceScreen(viewModel)
    }
}

@Composable
private fun AuthViaExternalServiceScreen(viewModel: BottomNavViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("AuthViaExternalServiceScreen")
    }
}