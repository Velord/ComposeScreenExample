package com.velord.usecase.movie.result

sealed class MovieLoadNewPageResult {
    data object Success : MovieLoadNewPageResult()
    data object Exausted : MovieLoadNewPageResult()
    class LoadPageFailed(val message: String) : MovieLoadNewPageResult()
}