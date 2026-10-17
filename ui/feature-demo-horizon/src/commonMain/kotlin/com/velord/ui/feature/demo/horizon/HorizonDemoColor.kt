package com.velord.ui.feature.demo.horizon

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.horizonbloom.HorizonBloomConfig

@Composable
internal fun HorizonColorSection(
    config: HorizonBloomConfig,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    WarmColorControl(
        color = config.warmColor,
        onAction = onAction,
    )
    CoolColorControl(
        color = config.coolColor,
        onAction = onAction,
    )
    BackgroundColorControl(
        color = config.backgroundColor,
        onAction = onAction,
    )
}

@Composable
private fun WarmColorControl(
    color: Color,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonColorPicker(
        label = "Warm Color",
        color = color,
        onColorChange = { onAction(HorizonDemoUiAction.ChangeWarmColor(it)) },
    )
}

@Composable
private fun CoolColorControl(
    color: Color,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonColorPicker(
        label = "Cool Color",
        color = color,
        onColorChange = { onAction(HorizonDemoUiAction.ChangeCoolColor(it)) },
    )
}

@Composable
private fun BackgroundColorControl(
    color: Color,
    onAction: (HorizonDemoUiAction) -> Unit,
) {
    HorizonColorPicker(
        label = "Background",
        color = color,
        onColorChange = { onAction(HorizonDemoUiAction.ChangeBackground(it)) },
    )
}

@Composable
private fun HorizonColorPicker(
    label: String,
    color: Color,
    onColorChange: (Color) -> Unit,
) {
    val hexState = remember(color) { mutableStateOf(color.toRgbHex()) }
    val dialogState = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = hexState.value,
        onValueChange = { value ->
            hexState.value = value
            parseRgbHex(value)?.let(onColorChange)
        },
        label = { Text(label) },
        singleLine = true,
        isError = parseRgbHex(hexState.value) == null,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        trailingIcon = {
            IconButton(onClick = { dialogState.value = true }) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(color, RoundedCornerShape(6.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(6.dp)),
                )
            }
        },
    )

    if (dialogState.value) {
        HorizonRgbDialog(
            label = label,
            color = color,
            onColorChange = onColorChange,
            onDismiss = { dialogState.value = false },
        )
    }
}

@Composable
private fun HorizonRgbDialog(
    label: String,
    color: Color,
    onColorChange: (Color) -> Unit,
    onDismiss: () -> Unit,
) {
    val maxChannel = RGB_CHANNEL_MAX.toFloat()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(label) },
        text = {
            Column {
                Text("RGB: ${color.toRgbHex()}")
                HorizonSlider(
                    label = "Red",
                    value = color.red * maxChannel,
                    range = 0f..maxChannel,
                    onValueChange = { onColorChange(color.copy(red = it / maxChannel)) },
                )
                HorizonSlider(
                    label = "Green",
                    value = color.green * maxChannel,
                    range = 0f..maxChannel,
                    onValueChange = { onColorChange(color.copy(green = it / maxChannel)) },
                )
                HorizonSlider(
                    label = "Blue",
                    value = color.blue * maxChannel,
                    range = 0f..maxChannel,
                    onValueChange = { onColorChange(color.copy(blue = it / maxChannel)) },
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        },
    )
}

@PreviewCombined
@Composable
private fun Preview() {
    HorizonColorSection(
        config = HorizonBloomConfig.DAWN,
        onAction = {},
    )
}
