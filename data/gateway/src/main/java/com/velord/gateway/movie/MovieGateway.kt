package com.velord.gateway.movie

import com.velord.appstate.AppStateDataSource
import com.velord.model.movie.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single

@Single
class MovieGateway(private val appState: AppStateDataSource) {

    fun getFlow(): Flow<List<Movie>> = appState.movieRosterFlow

    fun get(): List<Movie> = appState.movieRosterFlow.value

    fun clearInMemory() {
        appState.movieRosterFlow.value = emptyList()
    }

    fun update(function: (List<Movie>) -> List<Movie>) {
        appState.movieRosterFlow.update {
            function(it)
        }
    }
}
