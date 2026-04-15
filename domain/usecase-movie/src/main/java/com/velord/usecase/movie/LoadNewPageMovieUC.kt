package com.velord.usecase.movie

import com.velord.usecase.movie.model.MovieLoadNewPageResult

fun interface LoadNewPageMovieUC : suspend () -> MovieLoadNewPageResult
