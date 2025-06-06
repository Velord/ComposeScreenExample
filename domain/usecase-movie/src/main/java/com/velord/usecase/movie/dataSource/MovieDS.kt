package com.velord.usecase.movie.dataSource

import com.velord.model.movie.Movie
import kotlinx.coroutines.flow.Flow

interface MovieDS {
    fun getFlow(): Flow<List<Movie>>
    fun get(): List<Movie>
}