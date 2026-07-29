package com.velord.data.gateway.movie

import co.touchlab.kermit.Logger
import com.velord.data.appstate.AppStateDataSource
import com.velord.data.db.movie.dataSource.MovieDbDataSource
import com.velord.model.movie.Movie
import com.velord.usecase.movie.model.MovieFlow
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

private val log = Logger.withTag("MovieFavoriteGateway")

@Single
class MovieFavoriteGateway(
    private val appState: AppStateDataSource,
    private val db: MovieDbDataSource,
    private val movieSortGateway: MovieSortGateway,
) {

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        log.d { "CoroutineExceptionHandler: $throwable" }
    }
    private val scope = CoroutineScope(Job() + Dispatchers.IO + errorHandler)

    init {
        scope.launch {
            val sortType = movieSortGateway.getSelected().type
            db.getAllLikedFlow(sortType).collect {
                appState.movieFavoriteRosterFlow.value = it
            }
        }
    }

    fun getAll(): List<Movie> = appState.movieFavoriteRosterFlow.value

    fun getAllFlow(): MovieFlow = MovieFlow(appState.movieFavoriteRosterFlow)

    suspend fun update(movie: Movie) {
        val updated = movie.copy(isLiked = movie.isLiked.not())
        appState.movieRosterFlow.update { roster ->
            roster.map {
                if (it.id == updated.id) {
                    updated
                } else {
                    it
                }
            }
        }

        db.update(updated)
    }
}
