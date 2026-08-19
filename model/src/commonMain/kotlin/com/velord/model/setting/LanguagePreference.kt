package com.velord.model.setting

import com.velord.model.localization.LanguageCode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class LanguagePreference(val languageCode: LanguageCode) {

    @SerialName("default")
    DEFAULT(LanguageCode("default")),

    @SerialName("en")
    ENGLISH(LanguageCode("en")),

    @SerialName("es")
    SPANISH(LanguageCode("es"));

    val isDefault: Boolean get() = this == DEFAULT
}
