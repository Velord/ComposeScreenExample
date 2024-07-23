package com.velord.usecase.movie.result

sealed class UpdateMovieResult {
    data object Success : UpdateMovieResult()
    data class DbError(val message: String) : UpdateMovieResult()
}