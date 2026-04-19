package com.velord.gateway.movie.android

import co.touchlab.kermit.Logger
import com.velord.appstate.AppStateDataSource
import com.velord.db.movie.MovieDbDataSource
import com.velord.gateway.movie.MovieFavoriteReader
import com.velord.gateway.movie.MovieSortGateway
import com.velord.model.movie.Movie
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

private val log = Logger.withTag("MovieFavoriteGateway")

@Single(binds = [MovieFavoriteReader::class])
class MovieFavoriteGateway(
    private val appState: AppStateDataSource,
    private val db: MovieDbDataSource,
    private val movieSortGateway: MovieSortGateway,
) : MovieFavoriteReader {

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

    fun get(): List<Movie> = appState.movieFavoriteRosterFlow.value

    override fun getFlow(): Flow<List<Movie>> = appState.movieFavoriteRosterFlow

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
