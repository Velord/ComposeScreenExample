package com.velord.gateway.movie

import android.util.Log
import com.velord.appstate.AppStateService
import com.velord.db.MovieDbService
import com.velord.model.movie.Movie
import com.velord.usecase.movie.dataSource.MovieFavoriteDS
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

@Single
class MovieFavoriteGateway(
    private val appState: AppStateService,
    private val db: MovieDbService,
    private val movieSortGateway: MovieSortGateway
) : MovieFavoriteDS {

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("@@@@", "MovieFavoriteGateway CoroutineExceptionHandler: $throwable")
    }
    private val scope = CoroutineScope(Job() + Dispatchers.IO + errorHandler)

    init {
        scope.launch {
            // Do we need sorting here ?
            // We always need full list of liked movies
            val sortType = movieSortGateway.getSelected().type
            db.getAllLikedFlow(sortType).collect {
                appState.movieFavoriteRosterFlow.value = it
            }
        }
    }

    override fun get(): List<Movie> = appState.movieFavoriteRosterFlow.value

    override fun getFlow(): Flow<List<Movie>> = appState.movieFavoriteRosterFlow

    override suspend fun update(movie: Movie) {
        appState.movieRosterFlow.update { roster ->
            roster.map {
                if (it.id == movie.id) {
                    movie
                } else {
                    it
                }
            }
        }

        db.update(movie)
    }
}