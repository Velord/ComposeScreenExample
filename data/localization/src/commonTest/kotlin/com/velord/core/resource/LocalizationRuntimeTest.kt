package com.velord.core.resource

import com.velord.model.setting.LanguagePreference
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalizationRuntimeTest {

    @BeforeTest
    fun setUp() {
        LocalizationRuntime.resetForTest()
    }

    @Test
    fun `default preference uses Spanish device language`() {
        LocalizationRuntime.initialize(
            bundledJson = localizationJson(),
            remoteJson = null,
            preference = LanguagePreference.DEFAULT,
            deviceLanguageTag = "es-ES",
        )

        assertEquals("Configuración", LocalizationRuntime.getString(AppString.settings))
    }

    @Test
    fun `additional language is accepted without parser changes`() {
        LocalizationRuntime.initialize(
            bundledJson = localizationJson(
                additionalLanguages = """
                    ,"fr": {
                      "settings": "Paramètres",
                      "count": "Nombre : %1${'$'}d"
                    }
                """.trimIndent(),
            ),
            remoteJson = null,
            preference = LanguagePreference.DEFAULT,
            deviceLanguageTag = "fr-FR",
        )

        assertEquals("Paramètres", LocalizationRuntime.getString(AppString.settings))
    }

    @Test
    fun `unsupported device language falls back to English`() {
        LocalizationRuntime.initialize(
            bundledJson = localizationJson(),
            remoteJson = null,
            preference = LanguagePreference.DEFAULT,
            deviceLanguageTag = "uk-UA",
        )

        assertEquals("Settings", LocalizationRuntime.getString(AppString.settings))
    }

    @Test
    fun `explicit English overrides Spanish device language`() {
        LocalizationRuntime.initialize(
            bundledJson = localizationJson(),
            remoteJson = null,
            preference = LanguagePreference.ENGLISH,
            deviceLanguageTag = "es-ES",
        )

        assertEquals("Settings", LocalizationRuntime.getString(AppString.settings))
    }

    @Test
    fun `explicit Spanish overrides English device language`() {
        LocalizationRuntime.initialize(
            bundledJson = localizationJson(),
            remoteJson = null,
            preference = LanguagePreference.SPANISH,
            deviceLanguageTag = "en-US",
        )

        assertEquals("Configuración", LocalizationRuntime.getString(AppString.settings))
    }

    @Test
    fun `language preference switches current session without replacing document`() {
        LocalizationRuntime.initialize(
            bundledJson = localizationJson(),
            remoteJson = localizationJson(
                englishSettings = "Remote settings",
                spanishSettings = "Configuración remota",
            ),
            preference = LanguagePreference.ENGLISH,
        )

        LocalizationRuntime.setLanguagePreference(
            preference = LanguagePreference.SPANISH,
            deviceLanguageTag = "en-US",
        )

        assertEquals("Configuración remota", LocalizationRuntime.getString(AppString.settings))
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

        assertEquals("Application settings", LocalizationRuntime.getString(AppString.settings))
    }

    @Test
    fun `second initialization keeps first session document`() {
        LocalizationRuntime.initialize(
            bundledJson = localizationJson(),
            remoteJson = localizationJson(
                englishSettings = "Activated before launch",
                spanishSettings = "Activado antes del inicio",
            ),
            preference = LanguagePreference.ENGLISH,
        )
        LocalizationRuntime.initialize(
            bundledJson = localizationJson(),
            remoteJson = localizationJson(
                englishSettings = "Fetched during session",
                spanishSettings = "Obtenido durante la sesión",
            ),
            preference = LanguagePreference.SPANISH,
        )

        assertEquals("Activado antes del inicio", LocalizationRuntime.getString(AppString.settings))
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

        assertEquals("Settings", LocalizationRuntime.getString(AppString.settings))
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

        assertEquals("Cantidad: 7", LocalizationRuntime.getString(AppString.count, 7))
    }

    @Test
    fun `formatted string resolves positional arguments`() {
        LocalizationRuntime.initialize(
            bundledJson = localizationJson(),
            remoteJson = null,
            preference = LanguagePreference.ENGLISH,
        )

        assertEquals("Count: 12", LocalizationRuntime.getString(AppString.count, 12))
    }

    private fun localizationJson(
        englishSettings: String = "Settings",
        spanishSettings: String = "Configuración",
        additionalLanguages: String = "",
    ): String = """
        {
          "schemaVersion": 1,
          "languages": {
            "en": {
              "settings": "$englishSettings",
              "count": "Count: %1${'$'}d"
            },
            "es": {
              "settings": "$spanishSettings",
              "count": "Cantidad: %1${'$'}d"
            }
            $additionalLanguages
          }
        }
    """.trimIndent()
}
