package com.velord.buildlogic.util

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class BuildKonfigFlavorResolverTest {

    @Test
    fun `complete app variant resolves BuildKonfig flavor`() {
        val flavor = BuildKonfigFlavorResolver.resolve(listOf(":app:assembleProductionRelease"))

        assertEquals("productionRelease", flavor)
    }

    @Test
    fun `abbreviated app variant resolves BuildKonfig flavor`() {
        val flavorRoster = listOf(":app:assProdRel", ":app:aProdRel", ":app:aPR")
            .map { taskName -> BuildKonfigFlavorResolver.resolve(listOf(taskName)) }
        val expectedFlavorRoster = listOf(
            "productionRelease",
            "productionRelease",
            "productionRelease",
        )

        assertEquals(expectedFlavorRoster, flavorRoster)
    }

    @Test
    fun `single letter variant tokens resolve BuildKonfig flavor`() {
        val flavor = BuildKonfigFlavorResolver.resolve(listOf(":model:cDDK"))

        assertEquals("developDebug", flavor)
    }

    @Test
    fun `non build task keeps configured BuildKonfig default`() {
        val flavor = BuildKonfigFlavorResolver.resolve(listOf("allTests"))

        assertNull(flavor)
    }

    @Test
    fun `app task starting with ordinary ass text keeps configured BuildKonfig default`() {
        val flavor = BuildKonfigFlavorResolver.resolve(listOf(":app:assertQuality"))

        assertNull(flavor)
    }

    @Test
    fun `aggregate app build tasks are rejected`() {
        listOf(":app:assemble", ":app:a", ":app:assembleRelease", ":app:aR").forEach { taskName ->
            assertFailsWith<IllegalArgumentException> {
                BuildKonfigFlavorResolver.resolve(listOf(taskName))
            }
        }
    }

    @Test
    fun `aggregate app task cannot be masked by a complete variant`() {
        assertFailsWith<IllegalArgumentException> {
            BuildKonfigFlavorResolver.resolve(
                listOf(
                    ":app:assemble",
                    ":app:assembleProductionRelease",
                ),
            )
        }
    }

    @Test
    fun `one task resolving to multiple variants is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            BuildKonfigFlavorResolver.resolve(listOf(":app:aDevelopDebugProductionRelease"))
        }
    }

    @Test
    fun `abbreviated app variant cannot be combined with another variant`() {
        assertFailsWith<IllegalArgumentException> {
            BuildKonfigFlavorResolver.resolve(
                listOf(
                    ":app:aPR",
                    ":model:cDDK",
                ),
            )
        }
    }

    @Test
    fun `multiple app variants are rejected`() {
        assertFailsWith<IllegalArgumentException> {
            BuildKonfigFlavorResolver.resolve(
                listOf(
                    ":app:assembleDevelopDebug",
                    ":app:assembleProductionRelease",
                ),
            )
        }
    }
}
