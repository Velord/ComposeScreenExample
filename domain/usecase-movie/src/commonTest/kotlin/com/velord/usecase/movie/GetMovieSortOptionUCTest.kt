package com.velord.usecase.movie

import com.velord.model.movie.MovieSortOption
import com.velord.model.movie.SortType
import com.velord.usecase.movie.model.MovieSortOptionFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class GetMovieSortOptionUCTest {

    private val options = listOf(
        MovieSortOption(SortType.DateAscending, false),
        MovieSortOption(SortType.DateDescending, true)
    )

    @Test
    fun `invoke should return the exact sort option flow from delegate`() = runTest {
        val expectedFlow = flowOf(options)
        val useCase = GetMovieSortOptionUC {
            MovieSortOptionFlow(expectedFlow)
        }

        val result = useCase()

        assertSame(expectedFlow, result.flow)
        assertEquals(options, result.flow.first())
    }

    @Test
    fun `invoke should preserve all delegate emissions`() = runTest {
        val firstEmission = options.take(1)
        val secondEmission = options
        val useCase = GetMovieSortOptionUC {
            MovieSortOptionFlow(flowOf(firstEmission, secondEmission))
        }

        val result = useCase()

        assertEquals(listOf(firstEmission, secondEmission), result.flow.toList())
    }

    @Test
    fun `invoke should call delegate on each invocation`() = runTest {
        var invocationCount = 0
        val useCase = GetMovieSortOptionUC {
            invocationCount += 1
            MovieSortOptionFlow(flowOf(listOf(options[invocationCount - 1])))
        }

        val firstResult = useCase()
        val secondResult = useCase()

        assertEquals(2, invocationCount)
        assertEquals(listOf(options[0]), firstResult.flow.first())
        assertEquals(listOf(options[1]), secondResult.flow.first())
    }

    @Test
    fun `invoke should support empty sort option emission`() = runTest {
        val useCase = GetMovieSortOptionUC {
            MovieSortOptionFlow(flowOf(emptyList()))
        }

        val result = useCase()

        assertEquals(emptyList(), result.flow.first())
    }

    @Test
    fun `invoke should propagate delegate exception before flow creation`() {
        val useCase = GetMovieSortOptionUC {
            throw IllegalStateException("sort option flow failed")
        }

        val error = assertFailsWith<IllegalStateException> {
            useCase()
        }

        assertEquals("sort option flow failed", error.message)
    }
}
