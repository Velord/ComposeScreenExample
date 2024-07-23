package com.velord.usecase.movie

import com.velord.model.movie.MovieSortOption
import com.velord.model.movie.SortType
import com.velord.usecase.movie.dataSource.MovieSortDS
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SetMovieSortOptionUCTest {

    @Test
    fun `invoke should update movieSortDS with the selected option`() = runTest {
        val movieSortDS = mockk<MovieSortDS>(relaxed = true)
        val newOption = MovieSortOption(SortType.DateAscending, false)
        val setMovieSortOptionUC = SetMovieSortOptionUC(movieSortDS)

        setMovieSortOptionUC(newOption)

        coVerify { movieSortDS.update(newOption.copy(isSelected = true)) }
    }

    @Test
    fun `invoke should update movieSortDS with already selected option`() = runTest {
        val movieSortDS = mockk<MovieSortDS>(relaxed = true)
        val newOption = MovieSortOption(SortType.DateDescending, true)
        val setMovieSortOptionUC = SetMovieSortOptionUC(movieSortDS)

        setMovieSortOptionUC(newOption)

        coVerify { movieSortDS.update(newOption.copy(isSelected = true)) }
    }

    @Test
    fun `invoke should update movieSortDS with different options consecutively`() = runTest {
        val movieSortDS = mockk<MovieSortDS>(relaxed = true)
        val option1 = MovieSortOption(SortType.DateAscending, false)
        val option2 = MovieSortOption(SortType.DateDescending, false)
        val setMovieSortOptionUC = SetMovieSortOptionUC(movieSortDS)

        setMovieSortOptionUC(option1)
        setMovieSortOptionUC(option2)

        coVerify {
            movieSortDS.update(option1.copy(isSelected = true))
            movieSortDS.update(option2.copy(isSelected = true))
        }
    }

    @Test
    fun `invoke should update movieSortDS with the same option multiple times`() = runTest {
        val movieSortDS = mockk<MovieSortDS>(relaxed = true)
        val option = MovieSortOption(SortType.DateAscending, false)
        val setMovieSortOptionUC = SetMovieSortOptionUC(movieSortDS)

        repeat(3) { setMovieSortOptionUC(option) }

        coVerify(exactly = 3) { movieSortDS.update(option.copy(isSelected = true)) }
    }

    @Test
    fun `invoke should update movieSortDS with Default option`() = runTest {
        val movieSortDS = mockk<MovieSortDS>(relaxed = true)
        val setMovieSortOptionUC = SetMovieSortOptionUC(movieSortDS)
        setMovieSortOptionUC(MovieSortOption.Default)

        coVerify { movieSortDS.update(MovieSortOption.Default.copy(isSelected = true)) }
    }

    @Test
    fun `invoke should only update movieSortDS once per invocation`() = runTest {
        val movieSortDS = mockk<MovieSortDS>(relaxed = true)
        val option = MovieSortOption(SortType.DateDescending, false)
        val setMovieSortOptionUC = SetMovieSortOptionUC(movieSortDS)

        setMovieSortOptionUC(option)

        coVerify(exactly = 1) { movieSortDS.update(any()) }
    }
}