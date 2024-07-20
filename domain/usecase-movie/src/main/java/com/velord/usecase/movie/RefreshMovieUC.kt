package com.velord.usecase.movie

import com.velord.usecase.movie.dataSource.MovieDS

class RefreshMovieUC(private val dataSource: MovieDS) {

    operator fun invoke() {
        dataSource.refresh()
    }
}