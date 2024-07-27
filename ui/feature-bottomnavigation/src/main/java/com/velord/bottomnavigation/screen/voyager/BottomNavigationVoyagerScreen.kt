package com.velord.bottomnavigation.screen.voyager

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.bottomnavigation.viewmodel.BottomNavigationVoyagerVM
import org.koin.androidx.compose.koinViewModel

object BottomNavigationVoyagerScreen : Screen {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<BottomNavigationVoyagerVM>()
        VoyagerContent(viewModel)
    }
}