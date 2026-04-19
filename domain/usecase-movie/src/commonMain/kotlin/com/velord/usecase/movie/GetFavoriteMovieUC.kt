package com.velord.usecase.movie

import com.velord.usecase.movie.model.MovieFlow

/**
 * Returns a reactive roster of liked movies only.
 *
 * The returned [MovieFlow] is expected to stay up to date with like/unlike operations and with
 * sort-option changes applied to the favorite subset. This use case is read-only.
 */
fun interface GetFavoriteMovieUC : () -> MovieFlow
