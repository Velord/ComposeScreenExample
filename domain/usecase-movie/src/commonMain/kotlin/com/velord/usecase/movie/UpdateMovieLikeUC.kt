package com.velord.usecase.movie

import com.velord.model.movie.Movie

/**
 * Toggles the like state for the supplied movie snapshot.
 *
 * Callers should provide the movie as it is currently rendered to the user. Implementations are
 * expected to derive the opposite like state, update in-memory state, and persist the change.
 */
fun interface UpdateMovieLikeUC : suspend (Movie) -> Unit
