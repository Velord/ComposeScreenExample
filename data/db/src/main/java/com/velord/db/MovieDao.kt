package com.velord.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM MovieEntity")
    fun getAll(): Flow<List<MovieEntity>>

    @Update
    fun update(movie: MovieEntity)

    @Insert
    fun insertAll(vararg movies: MovieEntity)

    @Delete
    fun delete(movie: MovieEntity)
}
