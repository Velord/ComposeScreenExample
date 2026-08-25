package com.velord.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.velord.core.resource.LocalLocalizationState
import com.velord.ui.sharedviewmodel.LanguageVM
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LocalizationHost(
    languageVM: LanguageVM = koinViewModel(),
    content: @Composable () -> Unit,
) {
    val localizationState = languageVM.uiStateFlow
        .collectAsStateWithLifecycle()
        .value
        .localization

    CompositionLocalProvider(LocalLocalizationState provides localizationState) {
        content()
    }
}