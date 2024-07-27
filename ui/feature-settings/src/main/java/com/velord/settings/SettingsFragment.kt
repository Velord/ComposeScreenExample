package com.velord.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.velord.bottomnavigation.screen.jetpack.addTestCallback
import com.velord.bottomnavigation.viewmodel.BottomNavigationJetpackVM
import com.velord.navigation.fragment.entryPoint.SETTINGS_SOURCE
import com.velord.navigation.fragment.entryPoint.SettingsSourceFragment
import com.velord.resource.R
import com.velord.sharedviewmodel.ThemeViewModel
import com.velord.uicore.utils.LocalTheme
import com.velord.uicore.utils.setContentWithTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel by activityViewModels<ThemeViewModel>()
    private val viewModelBottom by viewModel<BottomNavigationJetpackVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        SettingsScreen(viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isActivated = this.arguments?.get(SETTINGS_SOURCE) == SettingsSourceFragment.SettingsGraph
        if (isActivated.not()) return
        addTestCallback("Settings graph", viewModelBottom)
    }
}

@Composable
fun SettingsScreen(viewModel: ThemeViewModel) {
    Content(
        onChangeAbideToOsTheme = { viewModel.onSwitchToOsTheme() },
        onChangeSystemTheme = { viewModel.onChangeDynamicTheme() },
        onChangeDarkTheme = { viewModel.onChangeDarkTheme() }
    )
}

@Composable
private fun Content(
    onChangeAbideToOsTheme: () -> Unit = {},
    onChangeSystemTheme: () -> Unit = {},
    onChangeDarkTheme: () -> Unit = {},
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
                onChange = onChangeAbideToOsTheme
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
                isEnabled = themeSwitcher.isSystemDynamicColorAvailable && themeSwitcher.config.abideToOs.not(),
                textWhenNotEnabled = disabledText.toString(),
                onChange = onChangeSystemTheme
            )

            ThemeSwitcher(
                title = stringResource(id = R.string.use_dark_theme),
                isChecked = themeSwitcher.config.useDarkTheme,
                isEnabled = themeSwitcher.config.abideToOs.not(),
                textWhenNotEnabled = disableOsStr,
                onChange = onChangeDarkTheme
            )
        }
    }
}

@Composable
private fun ThemeSwitcher(
    title: String,
    isChecked: Boolean,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    textWhenNotEnabled: String,
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
    Content()
}
