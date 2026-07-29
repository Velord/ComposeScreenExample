package com.velord.data.gateway.movie

import com.velord.data.appstate.AppStateDataSource
import com.velord.data.os.share.ShareDataSource
import com.velord.model.movie.Movie
import com.velord.usecase.movie.model.MovieFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single

@Single
class MovieGateway(
    private val appState: AppStateDataSource,
    private val shareDataSource: ShareDataSource,
) {

    fun getAllFlow(): MovieFlow = MovieFlow(appState.movieRosterFlow)

    fun getAll(): List<Movie> = appState.movieRosterFlow.value

    fun clearInMemory() {
        appState.movieRosterFlow.value = emptyList()
    }

    fun update(function: (List<Movie>) -> List<Movie>) {
        appState.movieRosterFlow.update {
            function(it)
        }
    }

    suspend fun share(movie: Movie) {
        shareDataSource.share(movie.toString())
    }
}
