package com.velord.usecase.movie

import com.velord.usecase.movie.model.MovieSortOptionFlow

/**
 * Returns the full roster of movie sort options together with their current selection state.
 *
 * Consumers should collect the returned [MovieSortOptionFlow] to react to future selection
 * changes instead of treating the value as a one-shot snapshot.
 */
fun interface GetMovieSortOptionUC : () -> MovieSortOptionFlow
