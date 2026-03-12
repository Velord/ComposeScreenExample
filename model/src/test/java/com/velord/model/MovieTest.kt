package com.velord.model

import com.velord.model.movie.Movie
import com.velord.model.movie.findRecentTimeInMilli
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class MovieTest {

    private val movieWithImagePath = Movie(
        1, "Movie Title", "Description", false,
        Movie.toCalendar("2023-11-15"), 4.5f, 100, "/image.jpg"
    )

    private val movieWithoutImagePath = Movie(
        2, "Another Movie", "Another Description", true,
        Movie.toCalendar("2024-02-20"), 3.8f, 50, null
    )

    @Test
    fun `formattedDateForCard returns correct format`() {
        val expectedDate = "2023 Nov 15"
        assertEquals(expectedDate, movieWithImagePath.formattedDateForCard)
    }

    @Test
    fun `formattedDateForDivider returns correct format`() {
        val expectedDate = "Nov 2023"
        assertEquals(expectedDate, movieWithImagePath.formattedDateForDivider)
    }

    @Test
    fun `isAnotherMonthOrYear returns true for different month`() {
        val differentMonthCalendar = Movie.toCalendar("2023-12-15")
        assertEquals(true, movieWithImagePath.isAnotherMonthOrYear(differentMonthCalendar))
    }

    @Test
    fun `isAnotherMonthOrYear returns true for different year`() {
        val differentYearCalendar = Movie.toCalendar("2024-11-15")
        assertEquals(true, movieWithImagePath.isAnotherMonthOrYear(differentYearCalendar))
    }

    @Test
    fun `isAnotherMonthOrYear returns false for same month and year`() {
        val sameMonthYearCalendar = Movie.toCalendar("2023-11-20")
        assertEquals(false, movieWithImagePath.isAnotherMonthOrYear(sameMonthYearCalendar))
    }

    @Test
    fun `imageUrl returns Picsum URL when imagePath is null`() {
        val expectedUrl = "https://picsum.photos/200/300"
        assertEquals(expectedUrl, movieWithoutImagePath.imageUrl)
    }

    @Test
    fun `imageUrl returns TMDB URL when imagePath is not null`() {
        val expectedUrl = "https://image.tmdb.org/t/p/original/image.jpg"
        assertEquals(expectedUrl, movieWithImagePath.imageUrl)
    }

    @Test
    fun `toCalendar converts date string to Calendar object`() {
        val calendar = Movie.toCalendar("2023-11-15")
        assertEquals(2023, calendar.get(Calendar.YEAR))
        assertEquals(10, calendar.get(Calendar.MONTH)) // Month is zero-based
        assertEquals(15, calendar.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun `toRaw converts Calendar object to date string`() {
        val calendar = Calendar.getInstance()
        calendar.set(2024, 1, 20) // February 20, 2024
        val expectedDateString = "2024-02-20"
        assertEquals(expectedDateString, Movie.toRaw(calendar))
    }

    @Test
    fun `findRecentTimeInMilli returns the most recent time in milliseconds`() {
        val movieList = listOf(movieWithImagePath, movieWithoutImagePath)
        val expectedTime = movieWithoutImagePath.date.timeInMillis // Movie without image path is more recent
        assertEquals(expectedTime, movieList.findRecentTimeInMilli())
    }

    @Test
    fun `formattedDateForCard handles single digit day and month`() {
        val movie = Movie(
            3, "Test Movie", "Description", false,
            Movie.toCalendar("2022-01-05"), 4.5f, 100, "/image.jpg"
        )
        val expectedDate = "2022 Jan 05"
        assertEquals(expectedDate, movie.formattedDateForCard)
    }

    @Test
    fun `formattedDateForDivider handles single digit month`() {
        val movie = Movie(
            4, "Test Movie", "Description", false,
            Movie.toCalendar("2021-05-15"), 4.5f, 100, "/image.jpg"
        )
        val expectedDate = "May 2021"
        assertEquals(expectedDate, movie.formattedDateForDivider)
    }

    @Test
    fun `isAnotherMonthOrYear handles null calendar input`() {
        assertEquals(true, movieWithImagePath.isAnotherMonthOrYear(null))
    }

    @Test
    fun `imageUrl handles empty imagePath`() {
        val movieWithEmptyImagePath = movieWithImagePath.copy(imagePath = "")
        val expectedUrl = "https://picsum.photos/200/300"
        assertEquals(expectedUrl, movieWithEmptyImagePath.imageUrl)
    }

    @Test
    fun `toCalendar handles invalid date format`() {
        val invalidCalendar = Movie.toCalendar("invalid-date")
        // Check if the calendar isset to the default time (current time) for invalid input
        val currentCalendar = Calendar.getInstance()
        assert(invalidCalendar.timeInMillis >= currentCalendar.timeInMillis) // Check if time is at or after current time
    }

    @Test
    fun `toRaw handles Calendar with default time`() {
        val defaultCalendar = Calendar.getInstance()
        // Reset the calendar to the default time (epoch)
        defaultCalendar.timeInMillis = 0
        val expectedDateString = "1970-01-01"
        assertEquals(expectedDateString, Movie.toRaw(defaultCalendar))
    }

    @Test
    fun `findRecentTimeInMilli handles empty movie list`() {
        val emptyMovieList = emptyList<Movie>()
        assertEquals(0, emptyMovieList.findRecentTimeInMilli())
    }

    @Test
    fun `findRecentTimeInMilli handles list with all movies having same date`() {
        val sameDateMovieList = listOf(
            movieWithImagePath,
            movieWithImagePath.copy(id = 3),
            movieWithImagePath.copy(id = 4)
        )
        val expectedTime = movieWithImagePath.date.timeInMillis
        assertEquals(expectedTime, sameDateMovieList.findRecentTimeInMilli())
    }

    @Test
    fun `findRecentTimeInMilli handles list with movies having empty dates`() {
        val emptyCalendar = Calendar.getInstance()
        emptyCalendar.clear() // Clear the calendar torepresent a missing date

        val movieWithEmptyDate = Movie(
            id = movieWithImagePath.id,
            title = movieWithImagePath.title,
            description = movieWithImagePath.description,
            isLiked = movieWithImagePath.isLiked,
            date = emptyCalendar, // Use an empty Calendar
            rating = movieWithImagePath.rating,
            voteCount = movieWithImagePath.voteCount,
            imagePath = movieWithImagePath.imagePath
        )
        val movieListWithEmptyDates = listOf(movieWithEmptyDate, movieWithoutImagePath)
        val expectedTime =movieWithoutImagePath.date.timeInMillis // Movie without image path should be most recent
        assertEquals(expectedTime, movieListWithEmptyDates.findRecentTimeInMilli())
    }
}