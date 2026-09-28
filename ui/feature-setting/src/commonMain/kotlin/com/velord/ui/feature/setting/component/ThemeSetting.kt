package com.velord.ui.feature.setting.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.velord.core.resource.AppString
import com.velord.core.resource.stringResource
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.core.ui.util.LocalTheme
import com.velord.model.setting.AppShapeStyle
import com.velord.model.setting.SpecialTheme
import com.velord.ui.sharedviewmodel.ThemeUiAction

private val SpecialTheme.titleRes get() = when (this) {
    SpecialTheme.LIGHT -> AppString.theme_light
    SpecialTheme.DARK -> AppString.theme_dark
    SpecialTheme.NEGATIVE_LIGHT -> AppString.theme_negative_light
    SpecialTheme.OCEAN -> AppString.theme_ocean
}

private val AppShapeStyle.titleRes get() = when (this) {
    AppShapeStyle.ROUNDED -> AppString.shape_rounded
    AppShapeStyle.CUT -> AppString.shape_cut
    AppShapeStyle.SQUARE -> AppString.shape_square
}

@Composable
internal fun ThemeSetting(onAction: (ThemeUiAction) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .padding(horizontal = 8.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AbideToOsSwitcher(onAction)
        DynamicThemeSwitcher(onAction)
        DarkThemeSwitcher(onAction)
        SpecialThemeRoster(onAction)
        ShapeStyleRoster(onAction)
    }
}

@Composable
private fun AbideToOsSwitcher(onThemeAction: (ThemeUiAction) -> Unit) {
    val themeSwitcher = LocalTheme.current

    val isEnabled = themeSwitcher.isSystemOsSwitchAvailable
    ThemeSwitcher(
        title = stringResource(AppString.abide_to_os_theme),
        isChecked = themeSwitcher.config.abideToOs,
        isEnabled = isEnabled,
        textWhenNotEnabled = stringResource(AppString.os_does_not_support_theme_switching),
        onChange = { onThemeAction(ThemeUiAction.AbideToOsThemeSwitch) },
    )
}

@Composable
private fun DynamicThemeSwitcher(onThemeAction: (ThemeUiAction) -> Unit) {
    SwitcherDivider()

    val themeSwitcher = LocalTheme.current

    val disabledText = StringBuilder()
    val disableOsStr = stringResource(AppString.disable_os_theme_switcher_first)
    if (themeSwitcher.isSystemDynamicColorAvailable.not()) {
        val android11Str = stringResource(AppString.not_available_on_android_11)
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
        title = stringResource(AppString.use_system_dynamic_theme),
        isChecked = themeSwitcher.config.useDynamicColor,
        isEnabled = isEnabled,
        textWhenNotEnabled = disabledText.toString(),
        modifier = Modifier.padding(top = 8.dp),
        onChange = { onThemeAction(ThemeUiAction.DynamicThemeSwitch) },
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
        title = stringResource(AppString.use_dark_theme),
        isChecked = themeSwitcher.config.useDarkTheme,
        isEnabled = isEnabled,
        textWhenNotEnabled = stringResource(AppString.disable_os_theme_switcher_first),
        modifier = Modifier.padding(top = 8.dp),
        onChange = { onThemeAction(ThemeUiAction.DarkThemeSwitch) },
    )
}

@Composable
private fun SpecialThemeRoster(onThemeAction: (ThemeUiAction) -> Unit) {
    val themeSwitcher = LocalTheme.current

    val isNotAvailableSystemOsSwitch = themeSwitcher.isSystemOsSwitchAvailable.not()
    val isNotAbideToOs = themeSwitcher.config.abideToOs.not()
    val isEnabled = isNotAvailableSystemOsSwitch || isNotAbideToOs
    if (isEnabled.not()) return

    val availableThemes = SpecialTheme.getAvailableThemeRoster(themeSwitcher.config.useDarkTheme)
    availableThemes.forEach { theme ->
        val isOptionEnabled = theme.isEnabled(themeSwitcher.config.useDynamicColor)
        Column {
            ThemeOption(
                title = stringResource(theme.titleRes),
                isSelected = themeSwitcher.config.current == theme,
                isEnabled = isOptionEnabled,
                onClick = { onThemeAction(ThemeUiAction.SpecialThemeSwitch(theme)) },
            )
            AnimatedVisibility(visible = isOptionEnabled.not()) {
                Text(
                    text = stringResource(AppString.disable_dynamic_theme_first),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun ShapeStyleRoster(onThemeAction: (ThemeUiAction) -> Unit) {
    SwitcherDivider()
    
    Text(
        text = stringResource(AppString.shape_style_title),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )

    val themeSwitcher = LocalTheme.current

    AppShapeStyle.entries.forEach { style ->
        ThemeOption(
            title = stringResource(style.titleRes),
            isSelected = themeSwitcher.config.shapeStyle == style,
            isEnabled = true,
            onClick = { onThemeAction(ThemeUiAction.ShapeStyleSwitch(style)) },
        )
    }
}

@Composable
private fun ThemeOption(
    title: String,
    isSelected: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                enabled = isEnabled,
                onClick = onClick,
            )
            .padding(vertical = 4.dp)
            .alpha(if (isEnabled) 1f else 0.5f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        RadioButton(
            selected = isSelected,
            enabled = isEnabled,
            onClick = null,
        )
        Text(text = title)
    }
}

@Composable
private fun ThemeSwitcher(
    title: String,
    isChecked: Boolean,
    isEnabled: Boolean,
    textWhenNotEnabled: String,
    modifier: Modifier = Modifier,
    onChange: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(text = title)
        AnimatedVisibility(visible = isEnabled.not()) {
            Text(
                text = textWhenNotEnabled,
                modifier = Modifier.animateContentSize(
                    animationSpec = tween(durationMillis = 800),
                ),
                color = MaterialTheme.colorScheme.error,
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = { onChange() },
            enabled = isEnabled,
        )
    }
}

@Composable
private fun SwitcherDivider() {
    HorizontalDivider(
        modifier = Modifier.clip(MaterialTheme.shapes.large),
        thickness = 2.5.dp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@PreviewCombined
@Composable
private fun Preview() {
    ThemeSetting(onAction = {})
}
