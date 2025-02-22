package com.velord.usecase.movie.dataSource

import com.velord.model.movie.Movie
import com.velord.model.movie.MovieRosterSize
import kotlinx.coroutines.flow.Flow

interface MovieDS {
    fun getFlow(): Flow<List<Movie>>
    fun get(): List<Movie>
    suspend fun loadNewPage(): MovieRosterSize
    suspend fun refresh(): MovieRosterSize
}