package com.velord.db

import android.content.Context
import androidx.room.Room
import com.velord.db.movie.MovieDao
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module

private fun createDatabase(context: Context): AppDatabase =
    Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "velord"
    ).fallbackToDestructiveMigration().build()

private fun provideMovieDao(database: AppDatabase): MovieDao = database.movieDao()

val databaseModule = module {
    single { createDatabase(get()) }
    single { provideMovieDao(get()) }
}

@Module
@ComponentScan("com.velord.db")
class DbModule