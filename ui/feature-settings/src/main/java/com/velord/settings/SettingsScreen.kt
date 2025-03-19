package com.velord.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
internal fun Content(onThemeAction: (ThemeUiAction) -> Unit) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(top = 16.dp)
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AbideToOsSwitcher(onThemeAction)
                DynamicThemeSwitcher(onThemeAction)
                DarkThemeSwitcher(onThemeAction)
            }
        }
    }
}

@Composable
private fun AbideToOsSwitcher(onThemeAction: (ThemeUiAction) -> Unit) {
    val themeSwitcher = LocalTheme.current

    val isEnabled = themeSwitcher.isSystemOsSwitchAvailable
    ThemeSwitcher(
        title = stringResource(id = R.string.abide_to_os_theme),
        isChecked = themeSwitcher.config.abideToOs,
        isEnabled = isEnabled,
        textWhenNotEnabled = stringResource(id = R.string.os_does_not_support_theme_switching),
        onChange = { onThemeAction(ThemeUiAction.AbideToOsThemeSwitch) }
    )
}

@Composable
private fun DynamicThemeSwitcher(onThemeAction: (ThemeUiAction) -> Unit) {
    SwitcherDivider()

    val themeSwitcher = LocalTheme.current

    val disabledText = StringBuilder()
    val disableOsStr = stringResource(id = R.string.disable_os_theme_switcher_first)
    if (themeSwitcher.isSystemDynamicColorAvailable.not()) {
        val android11Str = stringResource(id = R.string.not_available_on_android_11)
        disabledText.append(android11Str)
    }
    if (themeSwitcher.config.abideToOs) {
        if (disabledText.isNotEmpty()) disabledText.append("\n")
        disabledText.append(disableOsStr)
    }
    val isDynamicColorAvailable = themeSwitcher.isSystemDynamicColorAvailable
    val isNotAbideToOs = themeSwitcher.config.abideToOs.not()
    val isEnabled = isDynamicColorAvailable && isNotAbideToOs
    ThemeSwitcher(
        title = stringResource(id = R.string.use_system_dynamic_theme),
        isChecked = themeSwitcher.config.useDynamicColor,
        isEnabled = isEnabled,
        textWhenNotEnabled = disabledText.toString(),
        modifier = Modifier.padding(top = 8.dp),
        onChange = { onThemeAction(ThemeUiAction.DynamicThemeSwitch) }
    )
}

@Composable
private fun DarkThemeSwitcher(onThemeAction: (ThemeUiAction) -> Unit) {
    SwitcherDivider()

    val themeSwitcher = LocalTheme.current

    val isNotAvailableSystemOsSwitch = themeSwitcher.isSystemOsSwitchAvailable.not()
    val isNotAbideToOs = themeSwitcher.config.abideToOs.not()
    val isEnabled = isNotAvailableSystemOsSwitch || isNotAbideToOs
    ThemeSwitcher(
        title = stringResource(id = R.string.use_dark_theme),
        isChecked = themeSwitcher.config.useDarkTheme,
        isEnabled = isEnabled,
        textWhenNotEnabled = stringResource(id = R.string.disable_os_theme_switcher_first),
        modifier = Modifier.padding(top = 8.dp),
        onChange = { onThemeAction(ThemeUiAction.DarkThemeSwitch) }
    )
}

@Composable
private fun ThemeSwitcher(
    title: String,
    isChecked: Boolean,
    isEnabled: Boolean,
    textWhenNotEnabled: String,
    modifier: Modifier = Modifier,
    onChange: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = title)
        AnimatedVisibility(visible = isEnabled.not()) {
            Text(
                text = textWhenNotEnabled,
                modifier = Modifier.animateContentSize(animationSpec = tween(durationMillis = 800)),
                color = MaterialTheme.colorScheme.error,
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = { onChange() },
            enabled = isEnabled
        )
    }
}

@Composable
private fun SwitcherDivider() {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large),
        thickness = 2.5.dp
    )
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