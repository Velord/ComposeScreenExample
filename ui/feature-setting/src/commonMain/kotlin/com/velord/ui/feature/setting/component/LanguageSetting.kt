package com.velord.ui.feature.setting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.velord.core.resource.AppString
import com.velord.core.resource.stringResource
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.core.ui.compose.preview.previewLocalizationState
import com.velord.model.setting.LanguagePreference
import com.velord.ui.sharedviewmodel.LanguageUiAction
import com.velord.ui.sharedviewmodel.LanguageUiState

@Composable
internal fun LanguageSetting(
    uiState: LanguageUiState,
    onAction: (LanguageUiAction) -> Unit,
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

        LanguageOption(
            title = stringResource(AppString.language_default),
            isSelected = uiState.preference.isDefault,
            onClick = {
                onAction(LanguageUiAction.Select(LanguagePreference.DEFAULT))
            },
        )

        LanguagePreference.entries
            .filterNot { it.isDefault }
            .filter { it.languageCode in uiState.localization.document.languageRoster }
            .forEach { preference ->
                LanguageOption(
                    title = stringResource(
                        resource = AppString.language_name,
                        language = preference.languageCode,
                    ),
                    isSelected = uiState.preference == preference,
                    onClick = {
                        onAction(LanguageUiAction.Select(preference))
                    },
                )
            }
    }
}

@Composable
private fun LanguageOption(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
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


@PreviewCombined
@Composable
private fun Preview() {
    LanguageSetting(
        uiState = LanguageUiState(
            localization = previewLocalizationState,
            preference = LanguagePreference.DEFAULT,
        ),
        onAction = {},
    )
}
