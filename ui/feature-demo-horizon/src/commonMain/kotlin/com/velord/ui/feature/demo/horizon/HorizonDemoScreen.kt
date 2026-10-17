package com.velord.ui.feature.demo.horizon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.velord.core.ui.compose.component.PlatformScreenHeader
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.horizonbloom.HorizonBloom
import com.velord.horizonbloom.HorizonBloomConfig

@Composable
fun HorizonDemoScreen(
    viewModel: HorizonDemoVM,
    onBackClick: () -> Unit,
) {
    val uiState = viewModel.uiStateFlow.collectAsStateWithLifecycle()
    Content(
        config = uiState.value.config,
        onAction = viewModel::onAction,
        onBackClick = onBackClick,
    )
}

@Composable
private fun Content(
    config: HorizonBloomConfig,
    onAction: (HorizonDemoUiAction) -> Unit,
    onBackClick: () -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        val previewHeight = (maxHeight * 0.34f).coerceIn(180.dp, 300.dp)

        Column {
            PlatformScreenHeader(
                title = "Horizon Bloom",
                onBackClick = onBackClick,
            )
            HorizonPreview(
                config = config,
                height = previewHeight,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
            ) {
                HorizonCustomizeHeader(onAction)
                HorizonPresetSection(onAction)
                HorizonColorSection(
                    config = config,
                    onAction = onAction,
                )
                HorizonControlSection(
                    config = config,
                    onAction = onAction,
                )
                HorizonOptionSection(
                    config = config,
                    onAction = onAction,
                )
            }
        }
    }
}

@Composable
private fun HorizonPreview(
    config: HorizonBloomConfig,
    height: Dp,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(config.backgroundColor),
    ) {
        HorizonBloom(config = config)
    }
    Text(
        text = "Drag to shift the view. Tap the horizon to raise a glow.",
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
    )
}

@Composable
private fun HorizonCustomizeHeader(onAction: (HorizonDemoUiAction) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Customize",
            style = MaterialTheme.typography.titleLarge,
        )
        OutlinedButton(onClick = { onAction(HorizonDemoUiAction.Reset) }) {
            Text("Reset")
        }
    }
}

@Composable
private fun HorizonPresetSection(onAction: (HorizonDemoUiAction) -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedButton(onClick = { onAction(HorizonDemoUiAction.SelectDawn) }) {
            Text("Dawn")
        }
        OutlinedButton(onClick = { onAction(HorizonDemoUiAction.SelectTwilight) }) {
            Text("Twilight")
        }
        OutlinedButton(onClick = { onAction(HorizonDemoUiAction.SelectAurora) }) {
            Text("Aurora")
        }
    }
}

@Composable
private fun HorizonOptionSection(
    config: HorizonBloomConfig,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    AutoAuroraControl(
        value = config.autoAurora,
        onAction = onAction,
    )
    InteractiveControl(
        value = config.interactive,
        onAction = onAction,
    )
    IntroControl(
        value = config.intro,
        onAction = onAction,
    )
    PausedControl(
        value = config.paused,
        onAction = onAction,
    )
}

@Composable
private fun AutoAuroraControl(
    value: Boolean,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonToggle(
        label = "Auto Aurora",
        checked = value,
        onCheckedChange = { onAction(HorizonDemoUiAction.SetAutoAurora(it)) },
    )
}

@Composable
private fun InteractiveControl(
    value: Boolean,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonToggle(
        label = "Interactive",
        checked = value,
        onCheckedChange = { onAction(HorizonDemoUiAction.SetInteractive(it)) },
    )
}

@Composable
private fun IntroControl(
    value: Boolean,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonToggle(
        label = "Intro",
        checked = value,
        onCheckedChange = { onAction(HorizonDemoUiAction.SetIntro(it)) },
    )
}

@Composable
private fun PausedControl(
    value: Boolean,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonToggle(
        label = "Paused",
        checked = value,
        onCheckedChange = { onAction(HorizonDemoUiAction.SetPaused(it)) },
    )
}

@Composable
private fun HorizonToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    Content(
        config = HorizonBloomConfig.DAWN.copy(paused = true),
        onAction = {},
        onBackClick = {},
    )
}
