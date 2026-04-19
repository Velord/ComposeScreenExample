@file:Suppress("MatchingDeclarationName")

package com.velord.db

import com.velord.db.movie.MovieDao
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module

private fun createDatabase(): AppDatabase = appDatabaseBuilder()
    .fallbackToDestructiveMigration(false)
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()

private fun provideMovieDao(database: AppDatabase): MovieDao = database.movieDao()

val databaseModule = module {
    single { createDatabase() }
    single { provideMovieDao(get()) }
}

@Module
@ComponentScan("com.velord.db")
class DbModule
