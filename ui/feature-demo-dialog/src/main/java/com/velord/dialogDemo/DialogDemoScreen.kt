package com.velord.dialogDemo

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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.velord.core.resource.R
import com.velord.dialogDemo.component.DialogActions
import com.velord.dialogDemo.component.DialogAnimations
import com.velord.dialogDemo.component.DialogPredefinedAnimation
import com.velord.dialogDemo.component.OneButtonDialog
import com.velord.dialogDemo.component.TwoButtonDialog


@Composable
fun DialogDemoScreen(viewModel: DialogDemoViewModel) {
    val uiState = viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val isVisibleTwoButtonDialogState = rememberUpdatedState(uiState.value.isVisibleTwoButtonDialog)
    val isVisibleOneButtonDialogState = rememberUpdatedState(uiState.value.isVisibleOneButtonDialog)

    Content(onAction = viewModel::onAction)

    TwoButtonDialog(
        isVisibleState = isVisibleTwoButtonDialogState,
        title = "Two Button Dialog",
        action = DialogActions.TwoButton {
            viewModel.onAction(DialogDemoUiAction.TwoButtonDialogDismiss)
        }
    )

    OneButtonDialog(
        isVisibleState = isVisibleOneButtonDialogState,
        title = "One Button Dialog",
        action = DialogActions.OneButton {
            viewModel.onAction(DialogDemoUiAction.OneButtonDialogDismiss)
        },
        animation = DialogAnimations.Default.copy(
            mainBox = DialogPredefinedAnimation.Default()
        )
    )
}

@Composable
private fun Content(onAction: (DialogDemoUiAction) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OpenButton(
            text = stringResource(R.string.show_two_buttons_dialog),
            onClick = { onAction(DialogDemoUiAction.OpenTwoButtonDialogClick) }
        )
        OpenButton(
            text = stringResource(R.string.show_one_button_dialog),
            onClick = { onAction(DialogDemoUiAction.OpenOneButtonDialogClick) },
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

@Preview
@Composable
private fun ShapeDemoPreview() {
    Content(
        onAction = {}
    )
}