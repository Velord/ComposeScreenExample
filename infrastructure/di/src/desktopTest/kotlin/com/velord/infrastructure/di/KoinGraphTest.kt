package com.velord.infrastructure.di

import com.velord.usecase.movie.GetAllMovieUC
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.verify.verify
import kotlin.test.Test

private const val MEMORY_LOGGER_TYPE_NAME = "com.velord.data.os.memory.MemoryLogger"

@OptIn(KoinExperimentalAPI::class)
class KoinGraphTest : KoinTest {

    @Test
    fun `common app module roster should satisfy koin graph`() {
        val appModule = module {
            includes(createCommonAppModuleRoster())
        }

        val internalLogger = Class.forName(MEMORY_LOGGER_TYPE_NAME).kotlin
        appModule.verify(extraTypes = listOf(internalLogger))
    }

    @Test
    fun `common app module roster should create movie use case`() {
        startKoin {
            modules(createCommonAppModuleRoster())
        }

        try {
            get<GetAllMovieUC>()
        } finally {
            stopKoin()
        }
    }
}
