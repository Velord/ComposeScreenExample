package com.velord.gateway.movie

import com.velord.appstate.AppStateService
import com.velord.backend.ktor.MovieService
import com.velord.backend.model.MoviePageRequest
import com.velord.db.MovieDbService
import com.velord.model.movie.Movie
import com.velord.usecase.movie.dataSource.MovieDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single

@Single
class MovieGateway(
    private val appState: AppStateService,
    private val http: MovieService,
    private val db: MovieDbService
) : MovieDS {

    private var currentPage: Int = 1

    override fun getFlow(): Flow<List<Movie>> = appState.movieRosterFlow

    override fun get(): List<Movie> = appState.movieRosterFlow.value

    override fun update(movie: Movie) {
        appState.movieRosterFlow.update { roster ->
            roster.map {
                if (it.id == movie.id) {
                    movie
                } else {
                    it
                }
            }
        }
    }

    override suspend fun loadNewPage(): Int {
        val movieRoster = http.getMovie(MoviePageRequest(currentPage++))
        val newRoster = movieRoster.results.map { it.toDomain() }
        appState.movieRosterFlow.update { movies ->
            (movies + newRoster).toSet().toList()
        }

        return newRoster.size
    }

    override suspend fun refresh(): Int {
        appState.movieRosterFlow.value = emptyList()

        currentPage = 1
        return loadNewPage()
    }

    override suspend fun loadFromDB() {
        appState.movieRosterFlow.value = testMovieRoster
    }
}