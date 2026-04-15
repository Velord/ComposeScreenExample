package com.velord.usecase.movie

import com.velord.model.movie.MovieSortOption
import com.velord.usecase.movie.dataSource.MovieSortDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

fun interface GetMovieSortOptionUC : () -> Flow<List<MovieSortOption>>

class GetMovieSortOptionUCImpl(
    private val movieSortDS: MovieSortDS
) : GetMovieSortOptionUC {

    override operator fun invoke(): Flow<List<MovieSortOption>> = movieSortDS.getFlow().catch {}
}
