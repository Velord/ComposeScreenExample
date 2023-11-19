package com.velord.windyappflowsummator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.velord.windyappflowsummator.FlowSummatorViewModel.Companion.mapToCumulativeStringEachNumberByLine
import kotlinx.coroutines.launch

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
    val currentTextState = viewModel.sumFlow
        .mapToCumulativeStringEachNumberByLine()
        .collectAsStateWithLifecycle(initialValue = "")
    val currentEnteredValueState = viewModel.currentEnteredNumberFlow
        .collectAsStateWithLifecycle()

    val isStartEnabledState = remember {
        derivedStateOf { currentEnteredValueState.value != null }
    }

    Content(
        currentTextState = currentTextState,
        isStartEnabledState = isStartEnabledState,
        onStartClick = viewModel::onStartClick,
        enteredValueState = currentEnteredValueState,
        onNewEnteredValue = viewModel::onNewEnteredValue,
    )
}

@Composable
private fun Content(
    currentTextState: State<String>,
    isStartEnabledState: State<Boolean>,
    onStartClick: () -> Unit,
    enteredValueState: State<Int?>,
    onNewEnteredValue: (String) -> Unit
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
            isEnabled = isStartEnabledState.value,
            onClick = onStartClick
        )
        EnterField(
            value = enteredValueState.value,
            onNewValue = onNewEnteredValue
        )
        Result(text = currentTextState.value)
    }
    Info(

    )
}

@Composable
private fun Title() {
    Text(
        text = stringResource(id = R.string.flow_summator),
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
        modifier = Modifier.padding(16.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodySmall,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Info() {
    val infoDialogState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    InfoIcon {
        scope.launch {
            if (infoDialogState.isVisible) {
                infoDialogState.hide()
            } else {
                infoDialogState.show()
            }
        }
    }
    if (infoDialogState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {},
            sheetState = infoDialogState,
            windowInsets = WindowInsets(top = 0.dp),
        ) {
            Text(
                text = stringResource(id = R.string.info_description),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}

@Composable
private fun InfoIcon(onClick: () -> Unit) {
    Box(Modifier.fillMaxWidth()) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(16.dp)
                .clickable(onClick = onClick),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun FlowSummatorPreview() {
    Content(
        currentTextState = mutableStateOf("") ,
        isStartEnabledState = mutableStateOf(true),
        onStartClick = {},
        enteredValueState = mutableIntStateOf(4) ,
        onNewEnteredValue = {},
    )
}