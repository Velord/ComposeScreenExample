package com.velord.usecase.movie.result

import com.velord.model.movie.Movie
import kotlinx.coroutines.flow.Flow

sealed class GetMovieResult {

    data class Success(
        val flow: Flow<List<Movie>>
    ) : GetMovieResult()

    data class DBError(
        val flow: Flow<List<Movie>>,
        val message: String
    ) : GetMovieResult()

    data class MergeError(val message: String) : GetMovieResult()
}