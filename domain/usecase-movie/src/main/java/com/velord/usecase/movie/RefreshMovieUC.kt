package com.velord.usecase.movie

import android.util.Log
import com.velord.usecase.movie.dataSource.MovieDS

class RefreshMovieUC(private val dataSource: MovieDS) {

    suspend operator fun invoke() = try {
        dataSource.refresh()
        MovieResult.Success
    } catch (e: Exception) {
        Log.d("@@@", "RefreshMovieUC: ${e.message}")
        MovieResult.LoadPageFailed(e.message.orEmpty())
    }
}