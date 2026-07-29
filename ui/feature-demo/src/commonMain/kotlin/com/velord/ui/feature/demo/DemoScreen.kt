package com.velord.ui.feature.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velord.core.resource.Res
import com.velord.core.resource.demo
import com.velord.core.resource.open_dialog
import com.velord.core.resource.open_flow_summator
import com.velord.core.resource.open_hint_phone_number
import com.velord.core.resource.open_modifier_demo
import com.velord.core.resource.open_morph_demo
import com.velord.core.resource.open_movie
import com.velord.core.resource.open_shape_demo
import com.velord.core.ui.compose.component.PlatformScreenHeader
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.core.ui.util.ObserveSharedFlow
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoScreen(
    viewModel: DemoVM,
    onNavigationEvent: (DemoNavigationEvent) -> Unit,
    onGraphCompleted: () -> Unit,
    onBackClick: () -> Unit,
) {
    SideEffect {
        // Simulate we completed back stack handling
        onGraphCompleted()
    }

    ObserveSharedFlow(flow = viewModel.navigationEvent) {
        onNavigationEvent(it)
    }

    Content(
        onAction = viewModel::onAction,
        onBackClick = onBackClick,
    )
}

@Composable
private fun Content(
    onAction: (DemoUiAction) -> Unit,
    onBackClick: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PlatformScreenHeader(
            title = stringResource(Res.string.demo),
            onBackClick = onBackClick,
        )

        OpenButton(
            text = stringResource(Res.string.open_shape_demo),
            onClick = { onAction(DemoUiAction.OpenShapeClick) },
        )
        OpenButton(
            text = stringResource(Res.string.open_modifier_demo),
            onClick = { onAction(DemoUiAction.OpenModifierClick) },
        )
        OpenButton(
            text = stringResource(Res.string.open_flow_summator),
            onClick = { onAction(DemoUiAction.OpenSummatorClick) },
        )
        OpenButton(
            text = stringResource(Res.string.open_morph_demo),
            onClick = { onAction(DemoUiAction.OpenMorphClick) },
        )
        OpenButton(
            text = stringResource(Res.string.open_hint_phone_number),
            onClick = { onAction(DemoUiAction.OpenHintPhoneNumberClick) },
        )
        OpenButton(
            text = stringResource(Res.string.open_movie),
            onClick = { onAction(DemoUiAction.OpenMovieClick) },
        )
        OpenButton(
            text = stringResource(Res.string.open_dialog),
            onClick = { onAction(DemoUiAction.OpenDialogClick) },
        )
    }
}

@Composable
private fun OpenButton(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(top = 32.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        ),
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            style = MaterialTheme.typography.displaySmall.copy(fontSize = 22.sp),
        )
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    Content(
        onAction = {},
        onBackClick = {}
    )
}
