package com.velord.usecase.movie

import com.velord.usecase.movie.model.MovieLoadNewPageResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LoadNewPageMovieUCTest {

    @Test
    fun `invoke should return delegate result`() = runTest {
        val useCase = LoadNewPageMovieUC {
            MovieLoadNewPageResult.Success
        }

        val result = useCase()

        assertEquals(MovieLoadNewPageResult.Success, result)
    }

    @Test
    fun `invoke should call delegate on each invocation`() = runTest {
        var invocationCount = 0
        val useCase = LoadNewPageMovieUC {
            invocationCount += 1
            if (invocationCount == 1) {
                MovieLoadNewPageResult.Success
            } else {
                MovieLoadNewPageResult.Exhausted
            }
        }

        val firstResult = useCase()
        val secondResult = useCase()

        assertEquals(2, invocationCount)
        assertEquals(MovieLoadNewPageResult.Success, firstResult)
        assertEquals(MovieLoadNewPageResult.Exhausted, secondResult)
    }

    @Test
    fun `invoke should propagate delegate exception`() = runTest {
        val useCase = LoadNewPageMovieUC {
            throw IllegalStateException("load failed")
        }

        val error = assertFailsWith<IllegalStateException> {
            useCase()
        }

        assertEquals("load failed", error.message)
    }

    @Test
    fun `invoke should return load page failed result from delegate`() = runTest {
        val expectedResult = MovieLoadNewPageResult.LoadPageFailed("network error")
        val useCase = LoadNewPageMovieUC {
            expectedResult
        }

        val result = useCase()

        assertEquals(expectedResult, result)
    }

    @Test
    fun `invoke should support suspended delegate before returning`() = runTest {
        var completed = false
        val useCase = LoadNewPageMovieUC {
            delay(1)
            completed = true
            MovieLoadNewPageResult.Exhausted
        }

        val result = useCase()

        assertEquals(true, completed)
        assertEquals(MovieLoadNewPageResult.Exhausted, result)
    }
}
