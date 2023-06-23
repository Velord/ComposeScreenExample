package com.velord.composescreenexample.ui.main.demo

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
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import com.velord.uicore.R
import com.velord.uicore.utils.setContentWithTheme
import com.velord.util.fragment.viewLifecycleScope
import kotlinx.coroutines.launch

class DemoFragment : Fragment() {

    private val viewModel by viewModels<DemoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        DemoScreen(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserving()
    }

    private fun initObserving() {
        viewLifecycleScope.launch {
            viewModel.navigationEvent.collect {
                findNavController().navigate(it.id)
            }
        }
    }
}

@Composable
private fun DemoScreen(viewModel: DemoViewModel) {
    Content(
        onOpenShape = viewModel::onOpenShape,
        onOpenModifier = viewModel::onOpenModifier
    )
}

@Composable
private fun Content(
    onOpenShape: () -> Unit,
    onOpenModifier: () -> Unit,
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
            contentColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Text(
            text = text,
            modifier = Modifier,
            style = MaterialTheme.typography.displaySmall.copy(fontSize = 22.sp,)
        )
    }
}

@Preview
@Composable
private fun DemoPreview() {
    Content(
        onOpenShape = {},
        onOpenModifier = {}
    )
}