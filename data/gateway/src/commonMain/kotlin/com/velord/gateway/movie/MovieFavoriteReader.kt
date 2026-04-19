package com.velord.gateway.movie

import com.velord.model.movie.Movie
import kotlinx.coroutines.flow.Flow

interface MovieFavoriteReader {
    fun getFlow(): Flow<List<Movie>>
}
