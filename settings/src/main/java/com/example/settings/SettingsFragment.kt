package com.example.settings

import android.os.Build
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
import com.velord.uicore.utils.LocalThemeSwitcher
import com.velord.uicore.utils.setContentWithTheme
import com.velord.util.viewModel.ThemeViewModel

class SettingsFragment : Fragment() {

    private val viewModel by activityViewModels<ThemeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        SettingsScreen(viewModel)
    }
}

@Composable
private fun SettingsScreen(viewModel: ThemeViewModel) {
    val themeSwitcher = LocalThemeSwitcher.current
    Content(
        onChangeSystemTheme = { viewModel.changeSystemTheme(themeSwitcher) },
        onChangeDarkTheme = { viewModel.changeDarkTheme(themeSwitcher) }
    )
}

@Composable
private fun Content(
    onChangeSystemTheme: () -> Unit = {},
    onChangeDarkTheme: () -> Unit = {}
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
            val themeSwitcher = LocalThemeSwitcher.current
            val apiBiggerThanS = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
            ThemeSwitcher(
                title = stringResource(id = R.string.use_system_dynamic_theme),
                isChecked = themeSwitcher.useDynamicColor,
                isEnabled = apiBiggerThanS,
                onChange = onChangeSystemTheme
            )
            ThemeSwitcher(
                title = stringResource(id = R.string.use_dark_theme),
                isChecked = themeSwitcher.useDarkTheme,
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
    onChange: () -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            modifier = Modifier.padding(top = 8.dp, start = 16.dp)
        )
        if (isEnabled.not()) {
            Text(
                text = stringResource(id = R.string.not_available_on_android_11),
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
