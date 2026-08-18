package com.velord.data.firebase

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertNull

class DesktopFirebaseRemoteConfigDataSourceTest {

    @Test
    fun `desktop Remote Config fallback is safe to initialize fetch and read`() = runTest {
        val dataSource = createFirebaseRemoteConfigDataSource()

        dataSource.initialize(
            defaultLocalization = """
                {"schemaVersion":1,"languages":{"en":{},"es":{}}}
            """.trimIndent(),
        )
        dataSource.fetchAndActivate()

        assertNull(dataSource.getLocalization())
    }
}
