package com.velord.gateway.movie

import android.util.Log
import com.velord.backend.ktor.MovieService
import com.velord.model.movie.Movie
import com.velord.usecase.movie.dataSource.MovieDS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

@Single
class MovieGateway(
    private val service: MovieService
) : MovieDS {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val roster = MutableStateFlow<List<Movie>>(emptyList())

    init {
        initialize()
    }

    override fun getFlow(): Flow<List<Movie>> = roster

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

    private fun initialize() {
        if (roster.value.isEmpty()) {
            roster.value = testMovieRoster
        }

        scope.launch {
            try {
                service.getMovie()
            } catch (e: Exception) {
                Log.d("@@@", "Error: ${e.message}")
            }
        }
    }
}