package com.velord.usecase.movie

import com.velord.usecase.movie.dataSource.RefreshMovieDS
import com.velord.usecase.movie.result.MovieLoadNewPageResult

fun interface RefreshMovieUC : suspend () -> MovieLoadNewPageResult

class RefreshMovieUCImpl(
    private val dataSource: RefreshMovieDS
) : RefreshMovieUC {

    override suspend operator fun invoke() = try {
        dataSource.refresh()
        MovieLoadNewPageResult.Success
    } catch (e: Exception) {
        MovieLoadNewPageResult.LoadPageFailed(e.message.orEmpty())
    }
}
