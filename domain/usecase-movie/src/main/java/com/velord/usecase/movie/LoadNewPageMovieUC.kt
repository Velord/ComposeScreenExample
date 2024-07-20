package com.velord.usecase.movie

import android.util.Log
import com.velord.usecase.movie.dataSource.MovieDS

sealed class MovieResult {
    data object Success : MovieResult()
    class LoadPageFailed(val message: String) : MovieResult()
}

class LoadNewPageMovieUC(private val dataSource: MovieDS) {

    suspend operator fun invoke(): MovieResult = try {
        dataSource.loadNewPage()
        MovieResult.Success
    } catch (e: Exception) {
        Log.d("@@@", "LoadNewPageMovieUC: ${e.message}")
        MovieResult.LoadPageFailed(e.message.orEmpty())
    }
}