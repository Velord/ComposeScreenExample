package com.velord.usecase.movie

import com.velord.model.movie.MoviePagination
import com.velord.usecase.movie.dataSource.LoadNewPageMovieDS
import com.velord.usecase.movie.result.MovieLoadNewPageResult

fun interface LoadNewPageMovieUC : suspend () -> MovieLoadNewPageResult

class LoadNewPageMovieUCImpl(
    private val dataSource: LoadNewPageMovieDS
) : LoadNewPageMovieUC {

    override suspend operator fun invoke(): MovieLoadNewPageResult = try {
        val countOfNewItems = dataSource.load().value

        if (countOfNewItems < MoviePagination.PAGE_COUNT) {
            MovieLoadNewPageResult.Exhausted
        } else {
            MovieLoadNewPageResult.Success
        }
    } catch (e: Exception) {
        MovieLoadNewPageResult.LoadPageFailed(e.message.orEmpty())
    }
}
