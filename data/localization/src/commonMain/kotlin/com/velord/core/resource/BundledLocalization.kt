package com.velord.core.resource

import org.jetbrains.compose.resources.ExperimentalResourceApi

private const val LOCALIZATION_RESOURCE_PATH = "files/localization.json"

@OptIn(ExperimentalResourceApi::class)
suspend fun readBundledLocalizationJson(): String = Res
    .readBytes(LOCALIZATION_RESOURCE_PATH)
    .decodeToString()
