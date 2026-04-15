package com.velord.usecase.movie

import com.velord.model.movie.Movie
import com.velord.model.movie.SortType
import com.velord.usecase.movie.dataSource.MovieDS
import com.velord.usecase.movie.dataSource.MovieSortDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine

fun interface GetAllMovieUC: () -> Flow<List<Movie>>

class GetAllMovieUCImpl(
    private val movieDS: MovieDS,
    private val movieSortDS: MovieSortDS
) : GetAllMovieUC {

    override operator fun invoke(): Flow<List<Movie>> = mergeMovieWithSort()

    private fun mergeMovieWithSort(): Flow<List<Movie>> {
        val all = movieDS.getFlow()
        val sort = movieSortDS.getSelectedFlow()
        return all.combine(sort) { movies, sortOption ->
            when (sortOption.type) {
                SortType.DateDescending -> movies.sortedByDescending { it.date }
                SortType.DateAscending -> movies.sortedBy { it.date }
            }
        }.catch {}
    }
}
