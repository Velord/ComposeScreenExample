package com.velord.infrastructure.di

import com.velord.data.os.memory.MemoryLogger
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.verify.verify
import kotlin.test.Test

@OptIn(KoinExperimentalAPI::class)
class KoinGraphTest {

    @Test
    fun `common app module roster should satisfy koin graph`() {
        val appModule = module {
            includes(createCommonAppModuleRoster())
        }

        appModule.verify(extraTypes = listOf(MemoryLogger::class))
    }
}
