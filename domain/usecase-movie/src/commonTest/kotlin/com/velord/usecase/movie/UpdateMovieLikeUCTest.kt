package com.velord.usecase.movie

import com.velord.model.movie.Movie
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.time.Clock

class UpdateMovieLikeUCTest {

    private val movie = Movie(
        id = 1,
        title = "Movie Title",
        description = "Description",
        isLiked = false,
        date = Clock.System.now(),
        rating = 4.5f,
        voteCount = 100,
        imagePath = "image.jpg"
    )
    private val heterogeneousMovie = Movie(
        id = 99,
        title = "Silent Documentary",
        description = "No poster available",
        isLiked = true,
        date = Movie.toInstant("1984-04-14"),
        rating = 0.5f,
        voteCount = 1,
        imagePath = null
    )

    @Test
    fun `invoke should pass movie to delegate`() = runTest {
        var capturedMovie: Movie? = null
        val useCase = UpdateMovieLikeUC { updatedMovie ->
            capturedMovie = updatedMovie
        }

        useCase(movie)

        assertSame(movie, capturedMovie)
        assertEquals(movie, capturedMovie)
    }

    @Test
    fun `invoke should preserve movies across multiple invocations`() = runTest {
        val capturedMovies = mutableListOf<Movie>()
        val secondMovie = movie.copy(id = 2, isLiked = true)
        val useCase = UpdateMovieLikeUC { updatedMovie ->
            capturedMovies += updatedMovie
        }

        useCase(movie)
        useCase(secondMovie)

        assertEquals(listOf(movie, secondMovie), capturedMovies)
    }

    @Test
    fun `invoke should propagate delegate exception`() = runTest {
        val useCase = UpdateMovieLikeUC {
            throw IllegalStateException("update failed")
        }

        val error = assertFailsWith<IllegalStateException> {
            useCase(movie)
        }

        assertEquals("update failed", error.message)
    }

    @Test
    fun `invoke should forward already liked movie unchanged`() = runTest {
        var capturedMovie: Movie? = null
        val likedMovie = movie.copy(isLiked = true)
        val useCase = UpdateMovieLikeUC { updatedMovie ->
            capturedMovie = updatedMovie
        }

        useCase(likedMovie)

        assertSame(likedMovie, capturedMovie)
        assertEquals(likedMovie, capturedMovie)
    }

    @Test
    fun `invoke should support suspended delegate before completion`() = runTest {
        var completed = false
        val useCase = UpdateMovieLikeUC {
            delay(1)
            completed = true
        }

        useCase(movie)

        assertEquals(true, completed)
    }

    @Test
    fun `invoke should forward heterogeneous movies unchanged and in order`() = runTest {
        val capturedMovies = mutableListOf<Movie>()
        val useCase = UpdateMovieLikeUC { updatedMovie ->
            capturedMovies += updatedMovie
        }

        useCase(movie)
        useCase(heterogeneousMovie)

        assertEquals(listOf(movie, heterogeneousMovie), capturedMovies)
    }
}
