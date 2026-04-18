package com.velord.usecase.movie

import com.velord.model.movie.MovieSortOption
import com.velord.model.movie.SortType
import dev.mokkery.every
import dev.mokkery.mock
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetMovieSortOptionUCTest {

    @Test
    fun `invoke should return the flow from movieSortDS`() = runTest {
        val expectedOptions = listOf(
            MovieSortOption(SortType.DateAscending, false),
            MovieSortOption(SortType.DateDescending, true)
        )

        val movieSortDS = mock<MovieSortDS> {
            every { getFlow() } returns flow { emit(expectedOptions) }
        }

        val getMovieSortOptionUC = GetMovieSortOptionUCImpl(movieSortDS)
        val resultFlow = getMovieSortOptionUC()

        val actualOptions = resultFlow.first()

        assertEquals(expectedOptions, actualOptions)
    }

    @Test
    fun `invoke should return an empty flow when movieSortDS emits an empty flow`() = runTest {
        val movieSortDS = mock<MovieSortDS> {
            every { getFlow() } returns emptyFlow()
        }

        val getMovieSortOptionUC = GetMovieSortOptionUCImpl(movieSortDS)
        val resultFlow = getMovieSortOptionUC()

        val actualOptions = resultFlow.firstOrNull()

        assertEquals(null, actualOptions)
    }

    @Test
    fun `invoke should handle exceptions in movieSortDS getFlow and terminate`() = runTest {
        val expectedOptions = listOf(
            MovieSortOption(SortType.DateAscending, false)
        )

        val movieSortDS = mock<MovieSortDS> {
            every { getFlow() } returns flow {
                emit(expectedOptions)
                throw RuntimeException("Mocked Exception")
            }
        }

        val getMovieSortOptionUC = GetMovieSortOptionUCImpl(movieSortDS)
        val resultFlow = getMovieSortOptionUC()

        val actualOptions1 = resultFlow.first()
        val actualOptions2 = resultFlow.drop(1).firstOrNull()

        assertEquals(expectedOptions, actualOptions1)
        assertEquals(null, actualOptions2) // Assert flow termination after exception
    }

    @Test
    fun `invoke should emit multiple lists from movieSortDS`() = runTest {
        val expectedOptions1 = listOf(
            MovieSortOption(SortType.DateAscending, false)
        )
        val expectedOptions2 = listOf(
            MovieSortOption(SortType.DateAscending, false),
            MovieSortOption(SortType.DateDescending, true)
        )

        val movieSortDS = mock<MovieSortDS> {
            every { getFlow() } returns flow {
                emit(expectedOptions1)
                emit(expectedOptions2)
            }
        }

        val getMovieSortOptionUC = GetMovieSortOptionUCImpl(movieSortDS)
        val resultFlow = getMovieSortOptionUC()

        val actualOptions1 = resultFlow.first()
        val actualOptions2 = resultFlow.drop(1).first()

        assertEquals(expectedOptions1, actualOptions1)
        assertEquals(expectedOptions2, actualOptions2)
    }

    @Test
    fun `invoke should handle a delayed exception in movieSortDS getFlow and terminate`() = runTest {
        val expectedOptions = listOf(
            MovieSortOption(SortType.DateAscending, false)
        )

        val movieSortDS = mock<MovieSortDS> {
            every { getFlow() } returns flow {
                emit(expectedOptions)
                kotlinx.coroutines.delay(100)
                throw RuntimeException("Mocked Exception")
            }
        }

        val getMovieSortOptionUC = GetMovieSortOptionUCImpl(movieSortDS)
        val resultFlow = getMovieSortOptionUC()

        val actualOptions = resultFlow.first()
        val exceptionThrown = try {
            resultFlow.drop(1).first()
            false
        } catch (_: RuntimeException) {
            true
        }

        assertEquals(expectedOptions, actualOptions)
        assertTrue(exceptionThrown)
    }

    @Test
    fun `invoke should handle a flow that emits an empty list`() = runTest {
        val expectedOptions = emptyList<MovieSortOption>()

        val movieSortDS = mock<MovieSortDS> {
            every { getFlow() } returns flow { emit(expectedOptions) }
        }

        val getMovieSortOptionUC = GetMovieSortOptionUCImpl(movieSortDS)
        val resultFlow = getMovieSortOptionUC()

        val actualOptions = resultFlow.first()

        assertEquals(expectedOptions, actualOptions)
    }

    @Test
    fun `invoke should handle a flow with multiple empty list emissions`() = runTest {
        val movieSortDS = mock<MovieSortDS> {
            every { getFlow() } returns flow {
                emit(emptyList())
                emit(emptyList())
                emit(emptyList())
            }
        }

        val getMovieSortOptionUC = GetMovieSortOptionUCImpl(movieSortDS)
        val resultFlow = getMovieSortOptionUC()

        val emissionsCount = resultFlow.count()

        assertEquals(3, emissionsCount)
    }

    @Test
    fun `invoke should handle a flow with mixed empty and non-empty list emissions`() = runTest {
        val expectedOptions = listOf(
            MovieSortOption(SortType.DateDescending, true)
        )

        val movieSortDS = mock<MovieSortDS> {
            every { getFlow() } returns flow {
                emit(emptyList())
                emit(expectedOptions)
                emit(emptyList())
            }
        }

        val getMovieSortOptionUC = GetMovieSortOptionUCImpl(movieSortDS)
        val resultFlow = getMovieSortOptionUC()

        val actualOptions = resultFlow.drop(1).first()

        assertEquals(expectedOptions, actualOptions)
    }
}
