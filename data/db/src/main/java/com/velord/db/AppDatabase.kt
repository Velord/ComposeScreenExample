package com.velord.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.velord.db.movie.MovieDao
import com.velord.db.movie.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}