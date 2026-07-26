package com.velord.usecase.movie

import com.velord.model.movie.Movie

/**
 * Shares a movie through the current platform.
 */
fun interface ShareMovieUC : suspend (Movie) -> Unit
