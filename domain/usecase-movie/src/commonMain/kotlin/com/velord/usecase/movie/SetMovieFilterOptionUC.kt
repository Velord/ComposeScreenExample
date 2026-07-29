package com.velord.usecase.movie

import com.velord.model.movie.MovieFilterOption

fun interface SetMovieFilterOptionUC : suspend (MovieFilterOption) -> Unit
