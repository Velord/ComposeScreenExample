package com.velord.composescreenexample.ui.main.bottomNav

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.velord.composescreenexample.ui.compose.component.FullSizeBackground
import com.velord.composescreenexample.ui.compose.theme.GunPowder
import com.velord.composescreenexample.ui.compose.theme.RegularAmethystSmoke14Style
import com.velord.composescreenexample.ui.compose.theme.setContentWithTheme
import com.velord.composescreenexample.ui.compose.utils.getScreenWidthAndHeightInPx
import com.velord.model.utils.dpToPx
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
        BottomNavScreen(viewModel)
    }
}

@Composable
private fun BottomNavScreen(viewModel: BottomNavViewModel) {
    val (screenWidthPx, screenHeightPx) = getScreenWidthAndHeightInPx()
    val url = "https://picsum.photos/seed/BottomNavScreen/$screenWidthPx/$screenHeightPx"
    FullSizeBackground(url = url)

    Content()
}

@Composable
private fun Content() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Text(
            text = "AuthViaExternalServiceScreen",
            style = RegularAmethystSmoke14Style
        )
    }
}