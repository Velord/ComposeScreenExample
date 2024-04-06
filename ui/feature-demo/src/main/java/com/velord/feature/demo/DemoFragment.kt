package com.velord.feature.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.velord.bottomnavigation.addTestCallback
import com.velord.bottomnavigation.viewmodel.BottomNavigationJetpackVM
import com.velord.resource.R
import com.velord.uicore.utils.setContentWithTheme
import com.velord.util.fragment.viewLifecycleScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DemoFragment : Fragment() {

    private val viewModel by viewModels<DemoViewModel>()
    private val viewModelBottom by viewModel<BottomNavigationJetpackVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        DemoScreen(viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addTestCallback("Demo graph", viewModelBottom)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserving()
    }

    private fun initObserving() {
        viewLifecycleScope.launch {
            viewModel.navigationEventJetpack.collect {
                findNavController().navigate(it.id)
            }
        }
    }
}

@Composable
internal fun DemoScreen(viewModel: DemoViewModel) {
    Content(
        onOpenShape = viewModel::onOpenShape,
        onOpenModifier = viewModel::onOpenModifier,
        onOpenSummator = viewModel::onOpenSummator,
        onOpenMorph = viewModel::onOpenMorph
    )
}

@Composable
private fun Content(
    onOpenShape: () -> Unit,
    onOpenModifier: () -> Unit,
    onOpenSummator: () -> Unit,
    onOpenMorph: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OpenButton(text = stringResource(id = R.string.open_shape_demo), onClick = onOpenShape)
        OpenButton(text = stringResource(id = R.string.open_modifier_demo), onClick = onOpenModifier)
        OpenButton(text = stringResource(id = R.string.open_flow_summator), onClick = onOpenSummator)
        OpenButton(text = stringResource(id = R.string.open_morph_demo), onClick = onOpenMorph)
    }
}

@Composable
private fun OpenButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(top = 32.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            style = MaterialTheme.typography.displaySmall.copy(fontSize = 22.sp)
        )
    }
}

@Preview
@Composable
private fun DemoPreview() {
    Content(
        onOpenShape = {},
        onOpenModifier = {},
        onOpenSummator = {},
        onOpenMorph = {}
    )
}