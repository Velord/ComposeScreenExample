package com.velord.usecase.movie

import com.velord.usecase.movie.model.MovieFlow

/**
 * Returns a reactive roster of all available movies.
 *
 * The returned [MovieFlow] is expected to emit whenever the underlying movie roster changes,
 * including changes caused by persistence updates, pagination, refresh, or sort-option changes.
 * This use case is read-only and does not trigger loading by itself.
 */
fun interface GetAllMovieUC: () -> MovieFlow
