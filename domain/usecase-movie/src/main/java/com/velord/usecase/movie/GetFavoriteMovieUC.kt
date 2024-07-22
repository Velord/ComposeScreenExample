package com.velord.usecase.movie

import com.velord.model.movie.Movie
import com.velord.usecase.movie.dataSource.MovieFavoriteDS
import kotlinx.coroutines.flow.Flow

class GetFavoriteMovieUC(private val dataSource: MovieFavoriteDS) {
    operator fun invoke(): Flow<List<Movie>> = dataSource.getFlow()
}