package com.velord.usecase.movie

import android.util.Log
import com.velord.usecase.movie.dataSource.MovieDS
import com.velord.usecase.movie.result.MovieLoadNewPageResult

class RefreshMovieUC(private val dataSource: MovieDS) {

    suspend operator fun invoke() = try {
        dataSource.refresh()
        MovieLoadNewPageResult.Success
    } catch (e: Exception) {
        Log.d("@@@", "RefreshMovieUC: ${e.message}")
        MovieLoadNewPageResult.LoadPageFailed(e.message.orEmpty())
    }
}