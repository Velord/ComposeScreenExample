package com.velord.ui.feature.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.velord.core.resource.AppString
import com.velord.core.resource.stringResource
import com.velord.core.ui.compose.component.PlatformScreenHeader
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.core.ui.compose.preview.previewLocalizationState
import com.velord.model.setting.LanguagePreference
import com.velord.ui.feature.setting.component.LanguageSetting
import com.velord.ui.feature.setting.component.ThemeSetting
import com.velord.ui.sharedviewmodel.LanguageUiAction
import com.velord.ui.sharedviewmodel.LanguageUiState
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
    val languageUiState by languageViewModel.uiStateFlow.collectAsStateWithLifecycle()

    SideEffect {
        // Simulate we completed back stack handling
        onGraphCompleted()
    }

    Content(
        languageUiState = languageUiState,
        onThemeAction = themeViewModel::onAction,
        onLanguageAction = languageViewModel::onAction,
        onBackClick = onBackClick,
    )
}

@Composable
internal fun Content(
    languageUiState: LanguageUiState,
    onThemeAction: (ThemeUiAction) -> Unit,
    onLanguageAction: (LanguageUiAction) -> Unit,
    onBackClick: (() -> Unit),
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

            ThemeSetting(onThemeAction)
            LanguageSetting(
                uiState = languageUiState,
                onAction = onLanguageAction,
            )
        }
    }
}
@PreviewCombined
@Composable
private fun Preview() {
    Content(
        languageUiState = LanguageUiState(
            localization = previewLocalizationState,
            preference = LanguagePreference.DEFAULT,
        ),
        onThemeAction = {},
        onLanguageAction = {},
        onBackClick = {},
    )
}
