package com.velord.db.movie

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM MovieEntity")
    fun getAllFlow(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM MovieEntity")
    suspend fun getAll(): List<MovieEntity>

    @Query("SELECT * FROM MovieEntity WHERE isLiked = 1 " +
            "ORDER BY " +
            "CASE WHEN :orderBy = 'date' AND :sortOrder = 0 THEN date END DESC, " +
            "CASE WHEN :orderBy = 'date' AND :sortOrder = 1 THEN date END ASC "
    )
    fun getAllLikedFlow(
        orderBy: String,
        sortOrder: Int,
    ): Flow<List<MovieEntity>>

    @Query("SELECT * FROM MovieEntity WHERE " +
            "rating BETWEEN :ratingStart AND :ratingEnd " +
            "AND voteCount BETWEEN :voteCountStart AND :voteCountEnd " +
            "ORDER BY " +
            "CASE WHEN :orderBy = 'date' AND :sortOrder = 0 THEN date END DESC, " +
            "CASE WHEN :orderBy = 'date' AND :sortOrder = 1 THEN date END ASC " +
            "LIMIT :pageSize " +
            "OFFSET :offset"
    )
    suspend fun getFirstPage(
        ratingStart: Float,
        ratingEnd: Float,
        voteCountStart: Int,
        voteCountEnd: Int,
        orderBy: String,
        sortOrder: Int,
        pageSize: Int,
        offset: Int
    ): List<MovieEntity>

    @Update
    suspend fun update(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg movies: MovieEntity)

    @Delete
    suspend fun delete(movie: MovieEntity)

    @Query("DELETE FROM MovieEntity")
    suspend fun clear()
}
