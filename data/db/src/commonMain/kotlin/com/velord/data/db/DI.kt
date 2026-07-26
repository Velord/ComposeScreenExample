@file:Suppress("MatchingDeclarationName")

package com.velord.data.db

import com.velord.data.db.movie.MovieDao
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.velord.data.db")
class DbModule {

    @Single
    fun provideDatabase(): AppDatabase = appDatabaseBuilder()
        .fallbackToDestructiveMigration(false)
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()

    @Single
    fun provideMovieDao(database: AppDatabase): MovieDao = database.movieDao()
}