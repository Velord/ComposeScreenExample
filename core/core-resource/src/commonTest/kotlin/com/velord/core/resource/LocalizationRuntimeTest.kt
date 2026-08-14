package com.velord.core.resource

import com.velord.model.setting.LanguagePreference
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalizationRuntimeTest {

    @Test
    fun `default preference uses Spanish device language`() {
        LocalizationRuntime.initialize(
            bundledJson = localizationJson(),
            remoteJson = null,
            preference = LanguagePreference.DEFAULT,
            deviceLanguageTag = "es-ES",
        )

        assertEquals(
            expected = "Configuración",
            actual = LocalizationRuntime.getString(AppString.settings),
        )
    }

    @Test
    fun `unsupported device language falls back to English`() {
        LocalizationRuntime.initialize(
            bundledJson = localizationJson(),
            remoteJson = null,
            preference = LanguagePreference.DEFAULT,
            deviceLanguageTag = "uk-UA",
        )

        assertEquals(
            expected = "Settings",
            actual = LocalizationRuntime.getString(AppString.settings),
        )
    }

    @Test
    fun `explicit English overrides Spanish device language`() {
        LocalizationRuntime.initialize(
            bundledJson = localizationJson(),
            remoteJson = null,
            preference = LanguagePreference.ENGLISH,
            deviceLanguageTag = "es-ES",
        )

        assertEquals(
            expected = "Settings",
            actual = LocalizationRuntime.getString(AppString.settings),
        )
    }

    @Test
    fun `explicit Spanish overrides English device language`() {
        LocalizationRuntime.initialize(
            bundledJson = localizationJson(),
            remoteJson = null,
            preference = LanguagePreference.SPANISH,
            deviceLanguageTag = "en-US",
        )

        assertEquals(
            expected = "Configuración",
            actual = LocalizationRuntime.getString(AppString.settings),
        )
    }

    @Test
    fun `valid remote document replaces bundled document atomically`() {
        LocalizationRuntime.initialize(
            bundledJson = localizationJson(),
            remoteJson = localizationJson(
                englishSettings = "Application settings",
                spanishSettings = "Ajustes de la aplicación",
            ),
            preference = LanguagePreference.ENGLISH,
        )

        assertEquals(
            expected = "Application settings",
            actual = LocalizationRuntime.getString(AppString.settings),
        )
    }

    @Test
    fun `remote document with missing key falls back to bundled document`() {
        LocalizationRuntime.initialize(
            bundledJson = localizationJson(),
            remoteJson = """
                {
                  "schemaVersion": 1,
                  "languages": {
                    "en": {"settings": "Remote settings"},
                    "es": {"settings": "Configuración remota"}
                  }
                }
            """.trimIndent(),
            preference = LanguagePreference.ENGLISH,
        )

        assertEquals(
            expected = "Settings",
            actual = LocalizationRuntime.getString(AppString.settings),
        )
    }

    @Test
    fun `remote document with placeholder mismatch falls back to bundled document`() {
        val remote = localizationJson().replace(
            oldValue = "Cantidad: %1\$d",
            newValue = "Cantidad: %1\$s",
        )
        LocalizationRuntime.initialize(
            bundledJson = localizationJson(),
            remoteJson = remote,
            preference = LanguagePreference.SPANISH,
        )

        assertEquals(
            expected = "Cantidad: 7",
            actual = LocalizationRuntime.getString(AppString.count, 7),
        )
    }

    @Test
    fun `formatted string resolves positional arguments`() {
        LocalizationRuntime.initialize(
            bundledJson = localizationJson(),
            remoteJson = null,
            preference = LanguagePreference.ENGLISH,
        )

        assertEquals(
            expected = "Count: 12",
            actual = LocalizationRuntime.getString(AppString.count, 12),
        )
    }

    private fun localizationJson(
        englishSettings: String = "Settings",
        spanishSettings: String = "Configuración",
    ): String = """
        {
          "schemaVersion": 1,
          "languages": {
            "en": {
              "settings": "$englishSettings",
              "count": "Count: %1\$d"
            },
            "es": {
              "settings": "$spanishSettings",
              "count": "Cantidad: %1\$d"
            }
          }
        }
    """.trimIndent()
}
