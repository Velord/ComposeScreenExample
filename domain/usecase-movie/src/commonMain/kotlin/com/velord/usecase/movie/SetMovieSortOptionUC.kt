package com.velord.usecase.movie

import com.velord.model.movie.MovieSortOption

/**
 * Marks the supplied sort option as selected.
 *
 * Callers should pass the option that represents the desired final selection. Implementations are
 * expected to persist that choice and clear the selected flag from all competing options.
 */
fun interface SetMovieSortOptionUC : (MovieSortOption) -> Unit
