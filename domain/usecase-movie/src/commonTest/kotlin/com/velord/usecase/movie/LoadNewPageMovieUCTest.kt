package com.velord.usecase.movie

import com.velord.model.movie.MoviePagination
import com.velord.model.movie.MovieRosterSize
import com.velord.usecase.movie.dataSource.LoadNewPageMovieDS
import com.velord.usecase.movie.model.MovieLoadNewPageResult
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LoadNewPageMovieUCTest {

    @Test
    fun `invoke should return success when full page is loaded`() = runTest {
        val dataSource = mock<LoadNewPageMovieDS> {
            everySuspend { load() } returns MovieRosterSize(MoviePagination.PAGE_COUNT)
        }

        val result = LoadNewPageMovieUCImpl(dataSource).invoke()

        assertEquals(MovieLoadNewPageResult.Success, result)
    }

    @Test
    fun `invoke should return exhausted when partial page is loaded`() = runTest {
        val dataSource = mock<LoadNewPageMovieDS> {
            everySuspend { load() } returns MovieRosterSize(MoviePagination.PAGE_COUNT - 1)
        }

        val result = LoadNewPageMovieUCImpl(dataSource).invoke()

        assertEquals(MovieLoadNewPageResult.Exhausted, result)
    }

    @Test
    fun `invoke should return load page failed when datasource throws`() = runTest {
        val dataSource = mock<LoadNewPageMovieDS> {
            everySuspend { load() } throws RuntimeException("failed")
        }

        val result = LoadNewPageMovieUCImpl(dataSource).invoke()

        assertEquals(MovieLoadNewPageResult.LoadPageFailed("failed"), result)
    }
}
