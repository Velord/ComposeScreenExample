package com.example.windyappflowsummator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.velord.uicore.utils.setContentWithTheme

class FlowSummatorFragment : Fragment() {

    private val viewModel by viewModels<FlowSummatorViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        FlowSummatorScreen(viewModel)
    }
}

@Composable
private fun FlowSummatorScreen(viewModel: FlowSummatorViewModel) {
    val currentTextState = viewModel.currentTextFlow.collectAsStateWithLifecycle()
    val currentEnteredValueState = viewModel.currentEnteredNumberFlow.collectAsStateWithLifecycle()

    Content(
        currentText = currentTextState.value,
        isStartEnabled = currentEnteredValueState.value != null,
        onStartClick = viewModel::onStartClick,
        enteredValue = currentEnteredValueState.value,
        onNewEnteredValue = viewModel::onNewEnteredValue,
    )
}

@Composable
private fun Content(
    currentText: String,
    isStartEnabled: Boolean,
    onStartClick: () -> Unit,
    enteredValue: Int?,
    onNewEnteredValue: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Title()
        Start(
            isEnabled = isStartEnabled,
            onClick = onStartClick
        )
        EnterField(
            value = enteredValue,
            onNewValue = onNewEnteredValue
        )
        Result(text = currentText)
    }
}

@Composable
private fun Title() {
    Text(
        text = stringResource(id = R.string.app_name),
        modifier = Modifier.padding(top = 32.dp),
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
private fun Start(
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(top = 32.dp),
        enabled = isEnabled,
    ) {
        Text(text = stringResource(id = R.string.launch))
    }
}

@Composable
private fun EnterField(
    value: Int?,
    onNewValue: (String) -> Unit
) {
    TextField(
        value = value?.toString() ?: "",
        onValueChange = onNewValue,
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(horizontal = 32.dp),
        placeholder = { Text(text = stringResource(id = R.string.please_enter_the_number)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
private fun Result(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(top = 16.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodySmall,
    )
}

@Preview
@Composable
private fun FlowSummatorPreview() {
    Content(
        currentText = "",
        isStartEnabled = true,
        onStartClick = {},
        enteredValue = null,
        onNewEnteredValue = {},
    )
}