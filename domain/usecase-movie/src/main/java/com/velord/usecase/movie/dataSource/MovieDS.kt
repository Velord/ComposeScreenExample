package com.velord.usecase.movie.dataSource

import com.velord.model.movie.Movie
import kotlinx.coroutines.flow.Flow

interface MovieDS {
    fun getAll(): Flow<List<Movie>>
    fun update(movie: Movie)
}