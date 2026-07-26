package com.velord.usecase.movie.model

sealed class MovieLoadNewPageResult {
    data object Success : MovieLoadNewPageResult()
    data object Exhausted : MovieLoadNewPageResult()
    data class LoadPageFailed(val message: String) : MovieLoadNewPageResult()
}
