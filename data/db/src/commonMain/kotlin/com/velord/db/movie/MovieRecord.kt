package com.velord.db.movie

import com.velord.model.movie.Movie
import kotlinx.coroutines.flow.Flow

data class MovieRecord(
    val id: Int,
    val title: String,
    val description: String,
    val isLiked: Boolean,
    val date: String,
    val rating: Float,
    val voteCount: Int,
    val imagePath: String?,
)

internal interface MovieStore {
    suspend fun getFirstPage(
        ratingStart: Float,
        ratingEnd: Float,
        voteCountStart: Int,
        voteCountEnd: Int,
        orderBy: String,
        sortOrder: Int,
        pageSize: Int,
        offset: Int,
    ): List<MovieRecord>

    suspend fun insertAll(movies: List<MovieRecord>)
    suspend fun update(movie: MovieRecord)
    fun getAllLikedFlow(
        orderBy: String,
        sortOrder: Int,
    ): Flow<List<MovieRecord>>
    suspend fun clear()
}

internal fun MovieRecord.toDomain(): Movie = Movie(
    id = id,
    title = title,
    description = description,
    isLiked = isLiked,
    date = Movie.toInstant(date),
    rating = rating,
    voteCount = voteCount,
    imagePath = imagePath,
)

internal fun Movie.toRecord(): MovieRecord = MovieRecord(
    id = id,
    title = title,
    description = description,
    isLiked = isLiked,
    date = Movie.toRaw(date),
    rating = rating,
    voteCount = voteCount,
    imagePath = imagePath,
)
