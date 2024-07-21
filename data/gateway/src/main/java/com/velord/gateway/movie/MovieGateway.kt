package com.velord.gateway.movie

import com.velord.backend.ktor.MovieService
import com.velord.backend.model.MoviePageRequest
import com.velord.model.movie.Movie
import com.velord.usecase.movie.dataSource.MovieDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single

@Single
class MovieGateway(
    private val service: MovieService
) : MovieDS {

    private val roster = MutableStateFlow<List<Movie>>(emptyList())
    private var currentPage: Int = 1

    override fun getFlow(): Flow<List<Movie>> = roster

    override fun get(): List<Movie> = roster.value

    override fun update(movie: Movie) {
        roster.update { roster ->
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
        val movieRoster = service.getMovie(MoviePageRequest(currentPage++))
        val newRoster = movieRoster.results.map { it.toDomain() }
        roster.update { movies ->
            (movies + newRoster).toSet().toList()
        }

        return newRoster.size
    }

    override suspend fun refresh(): Int {
        roster.value = emptyList()

        currentPage = 1
        return loadNewPage()
    }

    override suspend fun loadFromDB() {
        roster.value = testMovieRoster
    }
}