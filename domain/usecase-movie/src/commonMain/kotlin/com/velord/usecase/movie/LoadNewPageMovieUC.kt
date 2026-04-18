package com.velord.usecase.movie

import com.velord.usecase.movie.model.MovieLoadNewPageResult

/**
 * Requests loading of the next movie page and appends any newly available items to the backing
 * roster.
 *
 * The returned [MovieLoadNewPageResult] reports whether a full page was loaded, the data source is
 * exhausted, or the operation failed. Implementations are expected to manage pagination state
 * internally.
 */
fun interface LoadNewPageMovieUC : suspend () -> MovieLoadNewPageResult
