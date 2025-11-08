package com.velord.feature.demo

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velord.core.resource.R
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.core.ui.utils.ObserveSharedFlow

@Composable
fun DemoScreen(
    viewModel: DemoViewModel,
    onNavigationEvent: (DemoDestinationNavigationEvent) -> Unit
) {
    ObserveSharedFlow(flow = viewModel.navigationEventDestination) {
        onNavigationEvent(it)
    }

    val context = LocalContext.current
    ObserveSharedFlow(flow = viewModel.toastEvent) {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }

    Content(onAction = viewModel::onAction)
}

@Composable
private fun Content(onAction: (DemoUiAction) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OpenButton(
            text = stringResource(id = R.string.open_shape_demo),
            onClick = { onAction(DemoUiAction.OpenShapeClick) }
        )
        OpenButton(
            text = stringResource(id = R.string.open_modifier_demo),
            onClick = { onAction(DemoUiAction.OpenModifierClick) }
        )
        OpenButton(
            text = stringResource(id = R.string.open_flow_summator),
            onClick = { onAction(DemoUiAction.OpenSummatorClick) }
        )
        OpenButton(
            text = stringResource(id = R.string.open_morph_demo),
            onClick = { onAction(DemoUiAction.OpenMorphClick) }
        )
        OpenButton(
            text = stringResource(id = R.string.open_hint_phone_number),
            onClick = { onAction(DemoUiAction.OpenHintPhoneNumberClick) }
        )
        OpenButton(
            text = stringResource(id = R.string.open_movie),
            onClick = { onAction(DemoUiAction.OpenMovieClick) }
        )
        OpenButton(
            text = stringResource(id = R.string.open_dialog),
            onClick = { onAction(DemoUiAction.OpenDialogClick) }
        )
    }
}

@Composable
private fun OpenButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(top = 32.dp),
        shape = RoundedCornerShape(10.dp),
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

@PreviewCombined
@Composable
private fun DemoPreview() {
    Content(onAction = {})
}