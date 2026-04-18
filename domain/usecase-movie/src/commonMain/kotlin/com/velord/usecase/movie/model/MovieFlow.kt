package com.velord.usecase.movie.model

import com.velord.model.movie.Movie
import com.velord.model.movie.MovieSortOption
import kotlinx.coroutines.flow.Flow

@JvmInline
value class MovieFlow(val flow: Flow<List<Movie>>)

@JvmInline
value class MovieSortOptionFlow(val flow: Flow<List<MovieSortOption>>)