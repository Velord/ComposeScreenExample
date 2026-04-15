package com.velord.usecase.movie

import com.velord.model.movie.Movie
import com.velord.model.movie.SortType
import com.velord.usecase.movie.dataSource.MovieFavoriteDS
import com.velord.usecase.movie.dataSource.MovieSortDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine

fun interface GetFavoriteMovieUC : () -> Flow<List<Movie>>

class GetFavoriteMovieUCImpl(
    private val movieFavoriteDS: MovieFavoriteDS,
    private val movieSortDS: MovieSortDS
) : GetFavoriteMovieUC {

    override operator fun invoke(): Flow<List<Movie>> = mergeMovieWithSort()

    private fun mergeMovieWithSort(): Flow<List<Movie>> {
        val favorite = movieFavoriteDS.getFlow()
        val sort = movieSortDS.getSelectedFlow()
        return favorite.combine(sort) { movies, sortOption ->
            when (sortOption.type) {
                SortType.DateDescending -> movies.sortedByDescending { it.date }
                SortType.DateAscending -> movies.sortedBy { it.date }
            }
        }.catch {}
    }
}
