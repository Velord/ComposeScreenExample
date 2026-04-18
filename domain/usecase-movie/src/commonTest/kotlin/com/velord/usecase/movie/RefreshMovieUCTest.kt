package com.velord.usecase.movie

import com.velord.usecase.movie.model.MovieLoadNewPageResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RefreshMovieUCTest {

    @Test
    fun `invoke should return delegate result`() = runTest {
        val useCase = RefreshMovieUC {
            MovieLoadNewPageResult.Success
        }

        val result = useCase()

        assertEquals(MovieLoadNewPageResult.Success, result)
    }

    @Test
    fun `invoke should call delegate on each invocation`() = runTest {
        var invocationCount = 0
        val useCase = RefreshMovieUC {
            invocationCount += 1
            if (invocationCount == 1) {
                MovieLoadNewPageResult.Success
            } else {
                MovieLoadNewPageResult.LoadPageFailed("refresh exhausted")
            }
        }

        val firstResult = useCase()
        val secondResult = useCase()

        assertEquals(2, invocationCount)
        assertEquals(MovieLoadNewPageResult.Success, firstResult)
        assertEquals(MovieLoadNewPageResult.LoadPageFailed("refresh exhausted"), secondResult)
    }

    @Test
    fun `invoke should propagate delegate exception`() = runTest {
        val useCase = RefreshMovieUC {
            throw IllegalStateException("refresh failed")
        }

        val error = assertFailsWith<IllegalStateException> {
            useCase()
        }

        assertEquals("refresh failed", error.message)
    }

    @Test
    fun `invoke should return load page failed result from delegate`() = runTest {
        val expectedResult = MovieLoadNewPageResult.LoadPageFailed("refresh denied")
        val useCase = RefreshMovieUC {
            expectedResult
        }

        val result = useCase()

        assertEquals(expectedResult, result)
    }

    @Test
    fun `invoke should support suspended delegate before returning`() = runTest {
        var completed = false
        val useCase = RefreshMovieUC {
            delay(1)
            completed = true
            MovieLoadNewPageResult.Success
        }

        val result = useCase()

        assertEquals(true, completed)
        assertEquals(MovieLoadNewPageResult.Success, result)
    }
}
