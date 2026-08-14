package com.velord.usecase.localization

import com.velord.model.localization.LocalizationStartup

fun interface InitializeLocalizationUC : suspend (String) -> LocalizationStartup
