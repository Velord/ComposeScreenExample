package com.velord.core.resource

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalInspectionMode
import com.velord.model.localization.LanguageCode
import com.velord.model.localization.LocalizationState

private val FORMAT_ARGUMENT_REGEX = Regex("""%([0-9]+)[$]([sd])""")

val LocalLocalizationState = staticCompositionLocalOf<LocalizationState?> { null }

@Composable
fun stringResource(
    resource: AppStringResource,
    vararg formatArgs: Any,
): String {
    // ?: in case of Preview
    val localization = localizationForStringResource() ?: return resource.key

    return getString(
        localization = localization,
        resource = resource,
        formatArgs = formatArgs,
    )
}

@Composable
fun stringResource(
    resource: AppStringResource,
    language: LanguageCode,
    vararg formatArgs: Any,
): String {
    // ?: in case of Preview
    val localization = localizationForStringResource() ?: return resource.key

    return getString(
        localization = localization,
        language = language,
        resource = resource,
        formatArgs = formatArgs,
    )
}

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
    val languageStringMap = checkNotNull(localization.document.languageRoster[language]) {
        "Localization language is missing: ${language.value}"
    }
    val template = languageStringMap[resource.key]
        ?: error("Localization resource is missing: ${resource.key}")

    return format(template, formatArgs)
}

@Composable
private fun localizationForStringResource(): LocalizationState? {
    val localization = LocalLocalizationState.current
    if (localization != null) return localization
    if (LocalInspectionMode.current) return null

    error("LocalizationState is not provided")
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
