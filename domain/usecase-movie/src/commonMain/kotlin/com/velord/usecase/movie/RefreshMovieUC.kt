package com.velord.usecase.movie

import com.velord.usecase.movie.model.MovieLoadNewPageResult

/**
 * Rebuilds the movie roster from the first page.
 *
 * Implementations are expected to clear any in-memory and persisted pagination state before
 * loading fresh data. The returned [MovieLoadNewPageResult] describes the refresh outcome.
 */
fun interface RefreshMovieUC : suspend () -> MovieLoadNewPageResult
