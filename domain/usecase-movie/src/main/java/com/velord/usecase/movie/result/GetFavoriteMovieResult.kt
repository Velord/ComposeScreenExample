package com.velord.usecase.movie.result

import com.velord.model.movie.Movie
import kotlinx.coroutines.flow.Flow

sealed class GetFavoriteMovieResult {
    data class Success(val flow: Flow<List<Movie>>) : GetFavoriteMovieResult()
    data class MergeError(val message: String) : GetFavoriteMovieResult()
}