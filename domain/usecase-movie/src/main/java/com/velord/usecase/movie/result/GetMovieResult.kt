package com.velord.usecase.movie.result

import com.velord.model.movie.Movie
import kotlinx.coroutines.flow.Flow

sealed class GetMovieResult(open val flow: Flow<List<Movie>>) {

    data class Success(
        override val flow: Flow<List<Movie>>
    ) : GetMovieResult(flow)

    data class DBError(
        override val flow: Flow<List<Movie>>,
        val message: String
    ) : GetMovieResult(flow)
}