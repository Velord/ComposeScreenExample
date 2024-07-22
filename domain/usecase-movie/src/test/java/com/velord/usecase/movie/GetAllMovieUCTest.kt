package com.velord.usecase.movie

import com.velord.usecase.movie.dataSource.MovieDS
import com.velord.usecase.movie.dataSource.MovieSortDS
import com.velord.usecase.movie.result.GetMovieResult
import java.util.Calendar

class GetAllMovieUCTest {


    @Test
    fun `invoke should return Success when movies are available`() = runTest {
        val movieDS = mockk<MovieDS> {
            coEvery { get() } returns listOf(
                Movie(1, "Movie 1", "Description 1", false, Calendar.getInstance(), 4.5f, 100),
                Movie(2, "Movie 2", "Description 2", true, Calendar.getInstance(), 3.8f, 50)
            )
            coEvery { loadFromDB() } just Runs
            every { getFlow() } returns flowOf(listOf(
                Movie(1, "Movie 1", "Description 1", false, Calendar.getInstance(), 4.5f, 100),
                Movie(2, "Movie 2", "Description 2", true, Calendar.getInstance(), 3.8f, 50)
            ))
        }
        val movieSortDS = mockk<MovieSortDS> {
            every { getSelectedFlow() } returns flowOf(SortOption(SortType.DateDescending))
        }

        val getAllMovieUC = GetAllMovieUC(movieDS, movieSortDS)
        val result = getAllMovieUC()

        assertTrue(result is GetMovieResult.Success)
        // Further assertions can be made on the result data if needed
    }
}