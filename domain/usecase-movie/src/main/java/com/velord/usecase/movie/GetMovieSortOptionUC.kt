package com.velord.usecase.movie

import com.velord.model.movie.MovieSortOption
import com.velord.usecase.movie.dataSource.MovieSortDS
import kotlinx.coroutines.flow.Flow

class GetMovieSortOptionUC(
    private val movieSortDS: MovieSortDS
) {
    operator fun invoke(): Flow<List<MovieSortOption>> = movieSortDS.getFlow()
}