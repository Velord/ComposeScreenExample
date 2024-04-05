package com.velord.bottomnavigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.bottomnavigation.screen.BottomNavigationVoyagerScreen
import com.velord.bottomnavigation.viewmodel.BottomNavViewModelVoyager
import org.koin.androidx.compose.koinViewModel

object BottomNavScreen : Screen {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<BottomNavViewModelVoyager>()
        BottomNavigationVoyagerScreen(viewModel)
    }
}