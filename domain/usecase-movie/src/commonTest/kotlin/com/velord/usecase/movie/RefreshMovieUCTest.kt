package com.velord.usecase.movie

import com.velord.model.movie.MovieRosterSize
import com.velord.usecase.movie.model.MovieLoadNewPageResult
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RefreshMovieUCTest {

    @Test
    fun `invoke should return success when refresh completes`() = runTest {
        val dataSource = mock<RefreshMovieDS> {
            everySuspend { refresh() } returns MovieRosterSize(1)
        }

        val result = RefreshMovieUCImpl(dataSource).invoke()

        assertEquals(MovieLoadNewPageResult.Success, result)
        verifySuspend(VerifyMode.exactly(1)) { dataSource.refresh() }
    }

    @Test
    fun `invoke should return load page failed when refresh throws`() = runTest {
        val dataSource = mock<RefreshMovieDS> {
            everySuspend { refresh() } throws RuntimeException("refresh error")
        }

        val result = RefreshMovieUCImpl(dataSource).invoke()

        assertEquals(MovieLoadNewPageResult.LoadPageFailed("refresh error"), result)
    }
}
