package com.velord.usecase.movie

import android.util.Log
import com.velord.model.movie.Movie
import com.velord.model.movie.SortType
import com.velord.usecase.movie.dataSource.MovieDS
import com.velord.usecase.movie.dataSource.MovieSortDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

sealed class GetMovieResult(open val flow: Flow<List<Movie>>) {

    data class Success(
        override val flow: Flow<List<Movie>>
    ) : GetMovieResult(flow)

    data class DBError(
        override val flow: Flow<List<Movie>>,
        val message: String
    ) : GetMovieResult(flow)
}

class GetAllMovieUC(
    private val movieDS: MovieDS,
    private val movieSortDS: MovieSortDS
) {

    suspend operator fun invoke(): GetMovieResult {
        val merged = mergeMovieWithSort()
        return try {
            if (movieDS.get().isEmpty()) {
                movieDS.loadFromDB()
            }

            GetMovieResult.Success(merged)
        } catch (e: Exception) {
            Log.d("@@@", "GetAllMovieUC: ${e.message}")
            GetMovieResult.DBError(merged, e.message.orEmpty())
        }
    }

    private fun mergeMovieWithSort(): Flow<List<Movie>> {
        val all = movieDS.getFlow()
        val sort = movieSortDS.getSelectedFlow()
        return all.combine(sort) { movies, sortOption ->
            when (sortOption.type) {
                SortType.DateDescending -> movies.sortedByDescending { it.date }
                SortType.DateAscending -> movies.sortedBy { it.date }
            }
        }
    }
}