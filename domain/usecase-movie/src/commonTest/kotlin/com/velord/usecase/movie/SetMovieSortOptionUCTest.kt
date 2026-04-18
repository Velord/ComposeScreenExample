package com.velord.usecase.movie

import com.velord.model.movie.MovieSortOption
import com.velord.model.movie.SortType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class SetMovieSortOptionUCTest {

    @Test
    fun `invoke should pass selected option to delegate`() {
        var capturedOption: MovieSortOption? = null
        val expectedOption = MovieSortOption(SortType.DateAscending, false)
        val useCase = SetMovieSortOptionUC { option ->
            capturedOption = option
        }

        useCase(expectedOption)

        assertSame(expectedOption, capturedOption)
        assertEquals(expectedOption, capturedOption)
    }

    @Test
    fun `invoke should preserve arguments across multiple invocations`() {
        val capturedOptions = mutableListOf<MovieSortOption>()
        val firstOption = MovieSortOption.Default
        val secondOption = MovieSortOption(SortType.DateDescending, true)
        val useCase = SetMovieSortOptionUC { option ->
            capturedOptions += option
        }

        useCase(firstOption)
        useCase(secondOption)

        assertEquals(listOf(firstOption, secondOption), capturedOptions)
    }

    @Test
    fun `invoke should propagate delegate exception`() {
        val useCase = SetMovieSortOptionUC {
            throw IllegalArgumentException("unsupported option")
        }

        val error = assertFailsWith<IllegalArgumentException> {
            useCase(MovieSortOption.Default)
        }

        assertEquals("unsupported option", error.message)
    }

    @Test
    fun `invoke should forward already selected option unchanged`() {
        var capturedOption: MovieSortOption? = null
        val expectedOption = MovieSortOption(SortType.DateDescending, true)
        val useCase = SetMovieSortOptionUC { option ->
            capturedOption = option
        }

        useCase(expectedOption)

        assertSame(expectedOption, capturedOption)
        assertEquals(expectedOption, capturedOption)
    }

    @Test
    fun `invoke should call delegate once per invocation`() {
        var invocationCount = 0
        val useCase = SetMovieSortOptionUC {
            invocationCount += 1
        }

        useCase(MovieSortOption.Default)
        useCase(MovieSortOption(SortType.DateAscending, false))
        useCase(MovieSortOption(SortType.DateDescending, true))

        assertEquals(3, invocationCount)
    }
}
