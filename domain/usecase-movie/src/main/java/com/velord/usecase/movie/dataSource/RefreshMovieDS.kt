package com.velord.usecase.movie.dataSource

import com.velord.model.movie.MovieRosterSize

interface RefreshMovieDS {
    suspend fun refresh(): MovieRosterSize
}