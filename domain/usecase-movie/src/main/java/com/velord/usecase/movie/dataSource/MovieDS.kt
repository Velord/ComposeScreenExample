package com.velord.usecase.movie.dataSource

import com.velord.model.profile.movie.Movie
import kotlinx.coroutines.flow.Flow

interface MovieDS {
    suspend fun getAll(): Flow<List<Movie>>
    fun update(movie: Movie)
}