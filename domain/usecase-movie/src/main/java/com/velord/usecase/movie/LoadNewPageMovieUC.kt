package com.velord.usecase.movie

import android.util.Log
import com.velord.model.movie.MoviePagination
import com.velord.usecase.movie.dataSource.MovieDS
import com.velord.usecase.movie.result.MovieLoadNewPageResult

class LoadNewPageMovieUC(private val dataSource: MovieDS) {

    suspend operator fun invoke(): MovieLoadNewPageResult = try {
        val countOfNewItems = dataSource.loadNewPage()
        Log.d("@@@", "LoadNewPageMovieUC: countOfNewItems: $countOfNewItems")

        if (countOfNewItems < MoviePagination.PAGE_COUNT) {
            MovieLoadNewPageResult.Exausted
        } else {
            MovieLoadNewPageResult.Success
        }
    } catch (e: Exception) {
        Log.d("@@@", "LoadNewPageMovieUC: ${e.message}")
        MovieLoadNewPageResult.LoadPageFailed(e.message.orEmpty())
    }
}