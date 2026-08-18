package com.velord.model.localization

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class LanguageCode(val value: String) {
    init {
        require(value.isNotBlank()) { "Language code must not be blank" }
    }

    companion object {
        val English = LanguageCode("en")
    }
}
