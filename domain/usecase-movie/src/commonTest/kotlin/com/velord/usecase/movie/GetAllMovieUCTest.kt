package com.velord.usecase.movie

import com.velord.model.movie.Movie
import com.velord.usecase.movie.model.MovieFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.time.Clock

class GetAllMovieUCTest {

    private val movies = listOf(
        Movie(
            id = 1,
            title = "Movie 1",
            description = "Description 1",
            isLiked = false,
            date = Clock.System.now(),
            rating = 4.5f,
            voteCount = 100,
            imagePath = "imagePath1"
        ),
        Movie(
            id = 2,
            title = "Movie 2",
            description = "Description 2",
            isLiked = true,
            date = Clock.System.now(),
            rating = 3.8f,
            voteCount = 50,
            imagePath = "imagePath2"
        )
    )
    private val heterogeneousMovies = listOf(
        Movie(
            id = 10,
            title = "Classic Drama",
            description = "Old movie",
            isLiked = false,
            date = Movie.toInstant("1999-12-31"),
            rating = 1.5f,
            voteCount = 7,
            imagePath = null
        ),
        Movie(
            id = 11,
            title = "Future Sci-Fi",
            description = "New movie",
            isLiked = true,
            date = Movie.toInstant("2025-08-20"),
            rating = 9.8f,
            voteCount = 9999,
            imagePath = "/poster.png"
        )
    )

    @Test
    fun `invoke should return the exact movie flow from delegate`() = runTest {
        val expectedFlow = flowOf(movies)
        val useCase = GetAllMovieUC { MovieFlow(expectedFlow) }

        val result = useCase()

        assertSame(expectedFlow, result.flow)
        assertEquals(movies, result.flow.first())
    }

    @Test
    fun `invoke should preserve all delegate emissions`() = runTest {
        val firstEmission = movies.take(1)
        val secondEmission = movies
        val useCase = GetAllMovieUC {
            MovieFlow(flowOf(firstEmission, secondEmission))
        }

        val result = useCase()

        assertEquals(listOf(firstEmission, secondEmission), result.flow.toList())
    }

    @Test
    fun `invoke should call delegate on each invocation`() = runTest {
        var invocationCount = 0
        val useCase = GetAllMovieUC {
            invocationCount += 1
            MovieFlow(flowOf(listOf(movies[invocationCount - 1])))
        }

        val firstResult = useCase()
        val secondResult = useCase()

        assertEquals(2, invocationCount)
        assertEquals(listOf(movies[0]), firstResult.flow.first())
        assertEquals(listOf(movies[1]), secondResult.flow.first())
    }

    @Test
    fun `invoke should support empty movie emission`() = runTest {
        val useCase = GetAllMovieUC {
            MovieFlow(flowOf(emptyList()))
        }

        val result = useCase()

        assertEquals(emptyList(), result.flow.first())
    }

    @Test
    fun `invoke should propagate delegate exception before flow creation`() {
        val useCase = GetAllMovieUC {
            throw IllegalStateException("movie flow failed")
        }

        val error = assertFailsWith<IllegalStateException> {
            useCase()
        }

        assertEquals("movie flow failed", error.message)
    }

    @Test
    fun `invoke should preserve heterogeneous movie payloads and order`() = runTest {
        val useCase = GetAllMovieUC {
            MovieFlow(flowOf(heterogeneousMovies))
        }

        val result = useCase()

        assertEquals(heterogeneousMovies, result.flow.first())
    }
}
