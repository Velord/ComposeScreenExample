package com.velord.usecase.movie

import com.velord.model.movie.Movie
import com.velord.model.movie.MovieSortOption
import com.velord.model.movie.SortType
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.time.Clock

class GetAllMovieUCTest {

    private val now = Clock.System.now()

    private val olderMovie = Movie(
        id = 1,
        title = "Movie 1",
        description = "Description 1",
        isLiked = false,
        date = now,
        rating = 4.5f,
        voteCount = 100,
        imagePath = "imagePath1"
    )
    private val newerMovie = Movie(
        id = 2,
        title = "Movie 2",
        description = "Description 2",
        isLiked = true,
        date = now.plus(1, DateTimeUnit.DAY, TimeZone.UTC),
        rating = 3.8f,
        voteCount = 50,
        imagePath = "imagePath2"
    )

    @Test
    fun `invoke should return movies sorted by descending date`() = runTest {
        val movieDS = mockk<MovieDS> {
            every { getFlow() } returns flowOf(listOf(olderMovie, newerMovie))
        }
        val movieSortDS = mockk<MovieSortDS> {
            every { getSelectedFlow() } returns flowOf(MovieSortOption(SortType.DateDescending, true))
        }

        val result = GetAllMovieUCImpl(movieDS, movieSortDS).invoke()

        assertTrue(result is GetMovieResult.Success)
        val movies = (result as GetMovieResult.Success).flow.first()
        assertEquals(listOf(newerMovie, olderMovie), movies)
    }

    @Test
    fun `invoke should return movies sorted by ascending date`() = runTest {
        val movieDS = mockk<MovieDS> {
            every { getFlow() } returns flowOf(listOf(newerMovie, olderMovie))
        }
        val movieSortDS = mockk<MovieSortDS> {
            every { getSelectedFlow() } returns flowOf(MovieSortOption(SortType.DateAscending, true))
        }

        val result = GetAllMovieUCImpl(movieDS, movieSortDS).invoke()

        assertTrue(result is GetMovieResult.Success)
        val movies = (result as GetMovieResult.Success).flow.first()
        assertEquals(listOf(olderMovie, newerMovie), movies)
    }

    @Test
    fun `invoke should return merge error when datasource throws immediately`() {
        val movieDS = mockk<MovieDS> {
            every { getFlow() } throws RuntimeException("boom")
        }
        val movieSortDS = mockk<MovieSortDS>()

        val result = GetAllMovieUCImpl(movieDS, movieSortDS).invoke()

        assertEquals(GetMovieResult.MergeError("boom"), result)
    }
}
