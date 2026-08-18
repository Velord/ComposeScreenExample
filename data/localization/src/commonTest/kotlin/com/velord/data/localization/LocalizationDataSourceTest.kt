package com.velord.data.localization

import com.velord.model.localization.LanguageCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LocalizationDataSourceTest {

    private val dataSource = LocalizationDataSource()

    @Test
    fun `parses declared default language`() {
        val document = dataSource.parse(localizationJson()).getOrThrow()

        assertEquals(LanguageCode("en"), document.defaultLanguage)
        assertEquals(setOf(LanguageCode("en"), LanguageCode("es")), document.languages.keys)
    }

    @Test
    fun `additional language needs no parser changes`() {
        val document = dataSource.parse(
            localizationJson(
                extraLanguages = """
                    ,"fr": {
                      "language_name": "Français",
                      "settings": "Paramètres",
                      "count": "Nombre : %1\$d"
                    }
                """.trimIndent(),
            )
        ).getOrThrow()

        assertTrue(LanguageCode("fr") in document.languages)
    }

    @Test
    fun `default language must be declared`() {
        assertFails {
            dataSource.parse(localizationJson(defaultLanguage = "de")).getOrThrow()
        }
    }

    @Test
    fun `all languages must have the same keys`() {
        val invalid = localizationJson().replace(
            "\"settings\": \"Configuración\",",
            "",
        )

        assertFails {
            dataSource.parse(invalid).getOrThrow()
        }
    }

    @Test
    fun `placeholder types must match default language`() {
        val invalid = localizationJson().replace(
            "Cantidad: %1\$d",
            "Cantidad: %1\$s",
        )

        assertFails {
            dataSource.parse(invalid).getOrThrow()
        }
    }

    @Test
    fun `remote may add a language`() {
        val bundled = dataSource.parse(localizationJson()).getOrThrow()
        val remote = localizationJson(
            extraLanguages = """
                ,"fr": {
                  "language_name": "Français",
                  "settings": "Paramètres",
                  "count": "Nombre : %1\$d"
                }
            """.trimIndent(),
        )

        assertNotNull(dataSource.parseRemote(remote, bundled))
    }

    @Test
    fun `remote cannot remove a bundled language`() {
        val bundled = dataSource.parse(localizationJson()).getOrThrow()
        val remote = """
            {
              "schemaVersion": 1,
              "defaultLanguage": "en",
              "languages": {
                "en": {
                  "language_name": "English",
                  "settings": "Remote settings",
                  "count": "Count: %1\$d"
                }
              }
            }
        """.trimIndent()

        assertNull(dataSource.parseRemote(remote, bundled))
    }

    @Test
    fun `remote cannot change default language`() {
        val bundled = dataSource.parse(localizationJson()).getOrThrow()
        val remote = localizationJson(defaultLanguage = "es")

        assertNull(dataSource.parseRemote(remote, bundled))
    }

    private fun localizationJson(
        defaultLanguage: String = "en",
        extraLanguages: String = "",
    ): String = """
        {
          "schemaVersion": 1,
          "defaultLanguage": "$defaultLanguage",
          "languages": {
            "en": {
              "language_name": "English",
              "settings": "Settings",
              "count": "Count: %1\$d"
            },
            "es": {
              "language_name": "Español",
              "settings": "Configuración",
              "count": "Cantidad: %1\$d"
            }
            $extraLanguages
          }
        }
    """.trimIndent()
}
