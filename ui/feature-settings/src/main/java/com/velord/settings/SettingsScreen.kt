package com.velord.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velord.resource.R
import com.velord.sharedviewmodel.ThemeUiAction
import com.velord.sharedviewmodel.ThemeViewModel
import com.velord.uicore.utils.LocalTheme

@Composable
fun SettingsScreen(viewModel: ThemeViewModel) {
    Content(onThemeAction = viewModel::onAction)
}

@Composable
internal fun Content(
    onThemeAction: (ThemeUiAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
    ) {
        Title(stringResource(id = R.string.settings))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .padding(horizontal = 8.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            val themeSwitcher = LocalTheme.current
            ThemeSwitcher(
                title = stringResource(id = R.string.abide_to_os_theme),
                isChecked = themeSwitcher.config.abideToOs,
                isEnabled = themeSwitcher.isSystemOsSwitchAvailable,
                textWhenNotEnabled = stringResource(id = R.string.os_does_not_support_theme_switching),
                onChange = { onThemeAction(ThemeUiAction.AbideToOsThemeSwitch) }
            )

            val disableOsStr = stringResource(id = R.string.disable_os_theme_switcher)
            val disabledText = StringBuilder()
            if (themeSwitcher.config.abideToOs) {
                disabledText.append(disableOsStr)
            }
            if (themeSwitcher.isSystemDynamicColorAvailable.not()) {
                val android11Str = stringResource(id = R.string.not_available_on_android_11)
                disabledText.append("\n" + android11Str)
            }
            ThemeSwitcher(
                title = stringResource(id = R.string.use_system_dynamic_theme),
                isChecked = themeSwitcher.config.useDynamicColor,
                textWhenNotEnabled = disabledText.toString(),
                isEnabled = themeSwitcher.isSystemDynamicColorAvailable && themeSwitcher.config.abideToOs.not(),
                onChange = { onThemeAction(ThemeUiAction.DynamicThemeSwitch) }
            )

            ThemeSwitcher(
                title = stringResource(id = R.string.use_dark_theme),
                isChecked = themeSwitcher.config.useDarkTheme,
                textWhenNotEnabled = disableOsStr,
                isEnabled = themeSwitcher.config.abideToOs.not(),
                onChange = { onThemeAction(ThemeUiAction.DarkThemeSwitch) }
            )
        }
    }
}

@Composable
private fun ThemeSwitcher(
    title: String,
    isChecked: Boolean,
    textWhenNotEnabled: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    onChange: () -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            modifier = Modifier.padding(top = 8.dp, start = 16.dp)
        )
        if (isEnabled.not()) {
            Text(
                text = textWhenNotEnabled,
                modifier = Modifier.padding(top = 8.dp, start = 16.dp),
                color = MaterialTheme.colorScheme.error
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = { onChange() },
            modifier = Modifier.padding(top = 8.dp, start = 16.dp),
            enabled = isEnabled
        )
    }
}

@Composable
private fun ColumnScope.Title(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .padding(top = 8.dp)
            .align(alignment = Alignment.CenterHorizontally),
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Preview
@Composable
private fun SettingsPreview() {
    Content(onThemeAction = {})
}