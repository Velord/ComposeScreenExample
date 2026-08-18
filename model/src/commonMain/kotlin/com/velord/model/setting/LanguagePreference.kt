package com.velord.model.setting

import com.velord.model.localization.LanguageCode
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@JvmInline
@Serializable(with = LanguagePreferenceSerializer::class)
value class LanguagePreference private constructor(
    val languageCode: String,
) {
    val isDefault: Boolean get() = languageCode.isEmpty()

    companion object {
        val DEFAULT = LanguagePreference("")

        fun language(languageCode: LanguageCode): LanguagePreference = LanguagePreference(languageCode.value)

        fun language(languageCode: String): LanguagePreference = LanguagePreference(languageCode)
    }
}

object LanguagePreferenceSerializer : KSerializer<LanguagePreference> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "LanguagePreference",
        kind = PrimitiveKind.STRING,
    )

    override fun serialize(encoder: Encoder, value: LanguagePreference) {
        encoder.encodeString(value.languageCode.ifEmpty { "default" })
    }

    override fun deserialize(decoder: Decoder): LanguagePreference = when (val value = decoder.decodeString()) {
        "", "default", "DEFAULT" -> LanguagePreference.DEFAULT
        "ENGLISH" -> LanguagePreference.language("en")
        "SPANISH" -> LanguagePreference.language("es")
        else -> LanguagePreference.language(value)
    }
}
