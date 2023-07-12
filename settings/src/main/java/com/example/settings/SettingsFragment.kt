package com.example.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.velord.uicore.utils.LocalThemeSwitcher
import com.velord.uicore.utils.ThemeSwitcher
import com.velord.uicore.utils.setContentWithTheme

class SettingsFragment : Fragment() {

    private val viewModel by activityViewModels<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme(viewModel.themeSwitcherFlow) {
        SettingsScreen(viewModel)
    }
}

@Composable
private fun SettingsScreen(viewModel: SettingsViewModel) {
    Content(
        onChangeThemeSwitcher = viewModel::changeTheme
    )
}

@Composable
private fun Content(
    onChangeThemeSwitcher: (ThemeSwitcher) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
    ) {
        Title(stringResource(id = R.string.settings))
        ThemeSwitcher(onChangeThemeSwitcher)
    }
}

@Composable
private fun ThemeSwitcher(
    onChange: (ThemeSwitcher) -> Unit
) {
    val themeSwitcher = LocalThemeSwitcher.current
    Text(
        text = "Use Dark theme",
        modifier = Modifier.padding(top = 8.dp, start = 16.dp)
    )
    Switch(
        checked = themeSwitcher.useDarkTheme,
        onCheckedChange = {
            onChange(
                ThemeSwitcher(
                    useDarkTheme = it,
                    useDynamicColor = themeSwitcher.useDynamicColor
                )
            )
        },
        modifier = Modifier.padding(top = 8.dp, start = 16.dp),
        enabled = themeSwitcher.useDynamicColor.not()
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
    Content()
}
