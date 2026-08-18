package com.velord.data.firebase

import kotlin.test.Test
import kotlin.test.assertNull

class DesktopFirebaseRemoteConfigDataSourceTest {

    @Test
    fun `desktop Remote Config fallback is safe to initialize fetch and read`() = runSuspend {
        val dataSource = createFirebaseRemoteConfigDataSource()

        dataSource.initialize(
            defaultLocalization = """
                {"schemaVersion":1,"languages":{"en":{},"es":{}}}
            """.trimIndent(),
        )
        dataSource.fetchAndActivate()

        assertNull(dataSource.getLocalization())
    }

    private fun runSuspend(block: suspend () -> Unit) {
        kotlinx.coroutines.runBlocking {
            block()
        }
    }
}
