package com.velord.usecase.movie

import com.velord.model.movie.MovieSortOption
import com.velord.model.movie.SortType
import dev.mokkery.MockMode
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class SetMovieSortOptionUCTest {

    @Test
    fun `invoke should update movieSortDS with the selected option`() = runTest {
        val movieSortDS = mock<MovieSortDS>(MockMode.autoUnit)
        val newOption = MovieSortOption(SortType.DateAscending, false)
        val setMovieSortOptionUC = SetMovieSortOptionUCImpl(movieSortDS)

        setMovieSortOptionUC(newOption)

        verifySuspend { movieSortDS.update(newOption.copy(isSelected = true)) }
    }

    @Test
    fun `invoke should update movieSortDS with already selected option`() = runTest {
        val movieSortDS = mock<MovieSortDS>(MockMode.autoUnit)
        val newOption = MovieSortOption(SortType.DateDescending, true)
        val setMovieSortOptionUC = SetMovieSortOptionUCImpl(movieSortDS)

        setMovieSortOptionUC(newOption)

        verifySuspend { movieSortDS.update(newOption.copy(isSelected = true)) }
    }

    @Test
    fun `invoke should update movieSortDS with different options consecutively`() = runTest {
        val movieSortDS = mock<MovieSortDS>(MockMode.autoUnit)
        val option1 = MovieSortOption(SortType.DateAscending, false)
        val option2 = MovieSortOption(SortType.DateDescending, false)
        val setMovieSortOptionUC = SetMovieSortOptionUCImpl(movieSortDS)

        setMovieSortOptionUC(option1)
        setMovieSortOptionUC(option2)

        verifySuspend {
            movieSortDS.update(option1.copy(isSelected = true))
            movieSortDS.update(option2.copy(isSelected = true))
        }
    }

    @Test
    fun `invoke should update movieSortDS with the same option multiple times`() = runTest {
        val movieSortDS = mock<MovieSortDS>(MockMode.autoUnit)
        val option = MovieSortOption(SortType.DateAscending, false)
        val setMovieSortOptionUC = SetMovieSortOptionUCImpl(movieSortDS)

        repeat(3) { setMovieSortOptionUC(option) }

        verifySuspend(VerifyMode.exactly(3)) { movieSortDS.update(option.copy(isSelected = true)) }
    }

    @Test
    fun `invoke should update movieSortDS with Default option`() = runTest {
        val movieSortDS = mock<MovieSortDS>(MockMode.autoUnit)
        val setMovieSortOptionUC = SetMovieSortOptionUCImpl(movieSortDS)
        setMovieSortOptionUC(MovieSortOption.Default)

        verifySuspend { movieSortDS.update(MovieSortOption.Default.copy(isSelected = true)) }
    }

    @Test
    fun `invoke should only update movieSortDS once per invocation`() = runTest {
        val movieSortDS = mock<MovieSortDS>(MockMode.autoUnit)
        val option = MovieSortOption(SortType.DateDescending, false)
        val setMovieSortOptionUC = SetMovieSortOptionUCImpl(movieSortDS)

        setMovieSortOptionUC(option)

        verifySuspend(VerifyMode.exactly(1)) { movieSortDS.update(any()) }
    }
}
