package com.velord.data.localization

import com.velord.model.localization.LanguageCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class LocalizationDataSourceTest {

    private val dataSource = LocalizationDataSource()

    @Test
    fun `parses declared default language`() {
        val document = dataSource.parse(localizationJson())

        assertEquals(LanguageCode("en"), document.defaultLanguage)
        assertEquals(setOf(LanguageCode("en"), LanguageCode("es")), document.languageRoster.keys)
    }

    @Test
    fun `unsupported bundled language fails parsing`() {
        val localization = localizationJson(
            extraLanguageRoster = """
                ,"fr": {
                  "language_name": "Français",
                  "settings": "Paramètres",
                  "count": "Nombre : %1${'$'}d"
                }
            """.trimIndent(),
        )

        assertFails {
            dataSource.parse(localization)
        }
    }

    @Test
    fun `default language must be declared`() {
        assertFails {
            dataSource.parse(localizationJson(defaultLanguage = "de"))
        }
    }

    @Test
    fun `all languages must have the same keys`() {
        val invalid = localizationJson().replace("\"settings\": \"Configuración\",", "")

        assertFails {
            dataSource.parse(invalid)
        }
    }

    @Test
    fun `placeholder types must match default language`() {
        val invalid = localizationJson().replace("Cantidad: %1\$d", "Cantidad: %1\$s")

        assertFails {
            dataSource.parse(invalid)
        }
    }

    @Test
    fun `remote cannot add an unsupported language`() {
        val bundled = dataSource.parse(localizationJson())
        val remote = localizationJson(
            extraLanguageRoster = """
                ,"fr": {
                  "language_name": "Français",
                  "settings": "Paramètres",
                  "count": "Nombre : %1${'$'}d"
                }
            """.trimIndent(),
        )

        assertFails {
            dataSource.parseRemote(remote, bundled)
        }
    }

    @Test
    fun `remote cannot remove a bundled language`() {
        val bundled = dataSource.parse(localizationJson())
        val remote = """
            {
              "schemaVersion": 1,
              "defaultLanguage": "en",
              "languageRoster": {
                "en": {
                  "language_name": "English",
                  "settings": "Remote settings",
                  "count": "Count: %1${'$'}d"
                }
              }
            }
        """.trimIndent()

        assertFails {
            dataSource.parseRemote(remote, bundled)
        }
    }

    @Test
    fun `remote cannot change default language`() {
        val bundled = dataSource.parse(localizationJson())
        val remote = localizationJson(defaultLanguage = "es")

        assertFails {
            dataSource.parseRemote(remote, bundled)
        }
    }

    @Test
    fun `malformed remote localization fails parsing`() {
        val bundled = dataSource.parse(localizationJson())

        assertFails {
            dataSource.parseRemote("{", bundled)
        }
    }

    private fun localizationJson(
        defaultLanguage: String = "en",
        extraLanguageRoster: String = "",
    ): String = """
        {
          "schemaVersion": 1,
          "defaultLanguage": "$defaultLanguage",
          "languageRoster": {
            "en": {
              "language_name": "English",
              "settings": "Settings",
              "count": "Count: %1${'$'}d"
            },
            "es": {
              "language_name": "Español",
              "settings": "Configuración",
              "count": "Cantidad: %1${'$'}d"
            }
            $extraLanguageRoster
          }
        }
    """.trimIndent()
}
