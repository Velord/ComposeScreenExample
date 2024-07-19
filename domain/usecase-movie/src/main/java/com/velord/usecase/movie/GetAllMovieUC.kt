package com.velord.usecase.movie

import com.velord.model.profile.movie.Movie
import com.velord.model.profile.movie.SortType
import com.velord.usecase.movie.dataSource.MovieDS
import com.velord.usecase.movie.dataSource.MovieSortDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetAllMovieUC(
    private val movieDS: MovieDS,
    private val movieSortDS: MovieSortDS
) {
    suspend operator fun invoke(): Flow<List<Movie>> {
        val all = movieDS.getAll()
        val sort = movieSortDS.getSelectedFlow()

        return all.combine(sort) { movies, sortOption ->
            when (sortOption.type) {
                SortType.DateDescending -> movies.sortedByDescending { it.date }
                SortType.DateAscending -> movies.sortedBy { it.date }
            }
        }
    }
}