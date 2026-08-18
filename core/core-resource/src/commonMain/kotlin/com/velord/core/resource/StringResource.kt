package com.velord.core.resource

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.velord.model.localization.LanguageCode
import com.velord.model.localization.LocalizationState

private val FORMAT_ARGUMENT_REGEX = Regex("""%([0-9]+)[$]([sd])""")

val LocalLocalizationState = staticCompositionLocalOf<LocalizationState?> { null }

@Composable
fun stringResource(
    resource: AppStringResource,
    vararg formatArgs: Any,
): String = LocalLocalizationState.current?.let { localization ->
    getString(
        localization = localization,
        resource = resource,
        formatArgs = formatArgs,
    )
} ?: format(
    template = defaultLocalizationStringRoster[resource.key] ?: resource.key,
    formatArgs = formatArgs,
)

fun getString(
    localization: LocalizationState,
    resource: AppStringResource,
    vararg formatArgs: Any,
): String = getString(
    localization = localization,
    language = localization.language,
    resource = resource,
    formatArgs = formatArgs,
)

fun getString(
    localization: LocalizationState,
    language: LanguageCode,
    resource: AppStringResource,
    vararg formatArgs: Any,
): String {
    val template = localization.document.languages[language]?.get(resource.key)
        ?: localization.document.languages[localization.document.defaultLanguage]?.get(resource.key)
        ?: defaultLocalizationStringRoster[resource.key]
        ?: resource.key
    return format(template, formatArgs)
}

private fun format(
    template: String,
    formatArgs: Array<out Any>,
): String = FORMAT_ARGUMENT_REGEX.replace(template) { match ->
    val argumentIndex = match.groupValues[1].toInt() - 1
    require(argumentIndex in formatArgs.indices) {
        "Missing format argument ${argumentIndex + 1} for: $template"
    }
    formatArgs[argumentIndex].toString()
}
