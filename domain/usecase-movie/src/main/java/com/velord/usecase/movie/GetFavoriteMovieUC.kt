package com.velord.usecase.movie

import com.velord.model.movie.Movie
import com.velord.model.movie.SortType
import com.velord.usecase.movie.dataSource.MovieFavoriteDS
import com.velord.usecase.movie.dataSource.MovieSortDS
import com.velord.usecase.movie.result.GetFavoriteMovieResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine

class GetFavoriteMovieUC(
    private val movieFavoriteDS: MovieFavoriteDS,
    private val movieSortDS: MovieSortDS
) {
    operator fun invoke(): GetFavoriteMovieResult = try {
        val merged = mergeMovieWithSort()
        GetFavoriteMovieResult.Success(merged)
    } catch (e: Exception) {
        GetFavoriteMovieResult.MergeError(e.message.orEmpty())
    }

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