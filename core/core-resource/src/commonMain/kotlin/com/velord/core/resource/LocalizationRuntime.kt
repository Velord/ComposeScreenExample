package com.velord.core.resource

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.velord.model.setting.LanguagePreference

private const val ENGLISH_LANGUAGE = "en"
private const val SPANISH_LANGUAGE = "es"
private val FORMAT_ARGUMENT_REGEX = Regex("""%(\d+)\${'$'}([sd])""")

private data class LocalizationState(
    val document: LocalizationDocument,
    val preference: LanguagePreference,
    val language: String,
)

object LocalizationRuntime {

    private var state by mutableStateOf<LocalizationState?>(null)

    fun initialize(
        bundledJson: String,
        remoteJson: String?,
        preference: LanguagePreference,
        deviceLanguageTag: String = currentLanguageTag(),
    ) {
        val bundled = LocalizationDocumentParser.parse(bundledJson).getOrThrow()
        val document = remoteJson
            ?.let { LocalizationDocumentParser.parseRemote(it, bundled) }
            ?: bundled
        state = LocalizationState(
            document = document,
            preference = preference,
            language = resolveLanguage(
                preference = preference,
                deviceLanguageTag = deviceLanguageTag,
                availableLanguages = document.languages.keys,
            ),
        )
    }

    fun setLanguagePreference(
        preference: LanguagePreference,
        deviceLanguageTag: String = currentLanguageTag(),
    ) {
        val currentState = state ?: return
        state = currentState.copy(
            preference = preference,
            language = resolveLanguage(
                preference = preference,
                deviceLanguageTag = deviceLanguageTag,
                availableLanguages = currentState.document.languages.keys,
            ),
        )
    }

    fun getString(
        resource: AppStringResource,
        vararg formatArgs: Any,
    ): String {
        val currentState = state
        val template = currentState
            ?.document
            ?.languages
            ?.get(currentState.language)
            ?.get(resource.key)
            ?: defaultLocalizationStringRoster[resource.key]
            ?: resource.key

        return format(template, formatArgs)
    }

    internal fun resolveLanguage(
        preference: LanguagePreference,
        deviceLanguageTag: String,
        availableLanguages: Set<String>,
    ): String = when (preference) {
        LanguagePreference.ENGLISH -> ENGLISH_LANGUAGE
        LanguagePreference.SPANISH -> SPANISH_LANGUAGE
        LanguagePreference.DEFAULT -> deviceLanguageTag
            .lowercase()
            .substringBefore('-')
            .substringBefore('_')
            .takeIf { it in availableLanguages }
            ?: ENGLISH_LANGUAGE
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
}
