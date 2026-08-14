package com.velord.ui.feature.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.velord.core.resource.AppString
import com.velord.core.resource.stringResource
import com.velord.core.ui.compose.component.PlatformScreenHeader
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.core.ui.util.LocalTheme
import com.velord.model.setting.LanguagePreference
import com.velord.ui.sharedviewmodel.LanguageUiAction
import com.velord.ui.sharedviewmodel.LanguageVM
import com.velord.ui.sharedviewmodel.ThemeUiAction
import com.velord.ui.sharedviewmodel.ThemeVM

@Composable
fun SettingScreen(
    themeViewModel: ThemeVM,
    languageViewModel: LanguageVM,
    onGraphCompleted: () -> Unit,
    onBackClick: (() -> Unit),
) {
    val languageUiState by languageViewModel.uiStateFlow.collectAsState()

    SideEffect {
        // Simulate we completed back stack handling
        onGraphCompleted()
    }

    Content(
        languagePreference = languageUiState.preference,
        onThemeAction = themeViewModel::onAction,
        onLanguageAction = languageViewModel::onAction,
        onBackClick = onBackClick,
    )
}

@Composable
internal fun Content(
    languagePreference: LanguagePreference = LanguagePreference.DEFAULT,
    onThemeAction: (ThemeUiAction) -> Unit,
    onLanguageAction: (LanguageUiAction) -> Unit = {},
    onBackClick: (() -> Unit)? = null,
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState()),
        ) {
            PlatformScreenHeader(
                title = stringResource(AppString.settings),
                onBackClick = onBackClick,
            )

            ThemeSettings(onThemeAction)
            LanguageSettings(
                languagePreference = languagePreference,
                onLanguageAction = onLanguageAction,
            )
        }
    }
}

@Composable
private fun ThemeSettings(onThemeAction: (ThemeUiAction) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .padding(horizontal = 8.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AbideToOsSwitcher(onThemeAction)
        DynamicThemeSwitcher(onThemeAction)
        DarkThemeSwitcher(onThemeAction)
    }
}

@Composable
private fun LanguageSettings(
    languagePreference: LanguagePreference,
    onLanguageAction: (LanguageUiAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .padding(horizontal = 8.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(AppString.language),
            style = MaterialTheme.typography.titleMedium,
        )
        LanguagePreference.entries.forEach { preference ->
            LanguageOption(
                preference = preference,
                isSelected = languagePreference == preference,
                onClick = {
                    onLanguageAction(LanguageUiAction.Select(preference))
                },
            )
        }
    }
}

@Composable
private fun LanguageOption(
    preference: LanguagePreference,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val title = when (preference) {
        LanguagePreference.DEFAULT -> stringResource(AppString.language_default)
        LanguagePreference.ENGLISH -> stringResource(AppString.language_english)
        LanguagePreference.SPANISH -> stringResource(AppString.language_spanish)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = onClick,
            )
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
        )
        Text(text = title)
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
    Content(onThemeAction = {})
}
