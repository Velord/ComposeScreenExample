package com.velord.gateway.movie

import com.velord.appstate.AppStateService
import com.velord.model.movie.Movie
import com.velord.usecase.movie.dataSource.MovieDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single

@Single
class MovieGateway(
    private val appState: AppStateService
) : MovieDS {

    override fun getFlow(): Flow<List<Movie>> = appState.movieRosterFlow

    override fun get(): List<Movie> = appState.movieRosterFlow.value

    fun clearInMemory() {
        appState.movieRosterFlow.value = emptyList()
    }

    fun update(function: (List<Movie>) -> List<Movie>) {
        appState.movieRosterFlow.update {
            function(it)
        }
    }
}