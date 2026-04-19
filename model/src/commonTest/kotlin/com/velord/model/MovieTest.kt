package com.velord.model

import com.velord.model.movie.Movie
import com.velord.model.movie.findRecentTimeInMilli
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock

class MovieTest {

    private val utc = TimeZone.UTC

    private val movieWithImagePath = Movie(
        1, "Movie Title", "Description", false,
        Movie.toInstant("2023-11-15"), 4.5f, 100, "/image.jpg"
    )

    private val movieWithoutImagePath = Movie(
        2, "Another Movie", "Another Description", true,
        Movie.toInstant("2024-02-20"), 3.8f, 50, null
    )

    @Test
    fun `formattedDateForCard returns correct format`() {
        val expectedDate = "2023 Nov 15"
        assertEquals(expectedDate, movieWithImagePath.formattedDateForCard(utc))
    }

    @Test
    fun `formattedDateForDivider returns correct format`() {
        val expectedDate = "Nov 2023"
        assertEquals(expectedDate, movieWithImagePath.formattedDateForDivider(utc))
    }

    @Test
    fun `isAnotherMonthOrYear returns true for different month`() {
        val differentMonth = Movie.toInstant("2023-12-15")
        assertEquals(true, movieWithImagePath.isAnotherMonthOrYear(differentMonth, utc))
    }

    @Test
    fun `isAnotherMonthOrYear returns true for different year`() {
        val differentYear = Movie.toInstant("2024-11-15")
        assertEquals(true, movieWithImagePath.isAnotherMonthOrYear(differentYear, utc))
    }

    @Test
    fun `isAnotherMonthOrYear returns false for same month and year`() {
        val sameMonthYear = Movie.toInstant("2023-11-20")
        assertEquals(false, movieWithImagePath.isAnotherMonthOrYear(sameMonthYear, utc))
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
    fun `toInstant converts date string to Instant at UTC midnight`() {
        val instant = Movie.toInstant("2023-11-15")
        val ldt = instant.toLocalDateTimeUtc()
        assertEquals(2023, ldt.year)
        assertEquals(11, ldt.monthNumber)
        assertEquals(15, ldt.dayOfMonth)
        assertEquals(0, ldt.hour)
    }

    @Test
    fun `toRaw converts Instant to date string`() {
        val instant = Movie.toInstant("2024-02-20")
        assertEquals("2024-02-20", Movie.toRaw(instant))
    }

    @Test
    fun `findRecentTimeInMilli returns the most recent time in milliseconds`() {
        val movieList = listOf(movieWithImagePath, movieWithoutImagePath)
        val expectedTime = movieWithoutImagePath.date.toEpochMilliseconds()
        assertEquals(expectedTime, movieList.findRecentTimeInMilli())
    }

    @Test
    fun `formattedDateForCard handles single digit day and month`() {
        val movie = Movie(
            3, "Test Movie", "Description", false,
            Movie.toInstant("2022-01-05"), 4.5f, 100, "/image.jpg"
        )
        val expectedDate = "2022 Jan 05"
        assertEquals(expectedDate, movie.formattedDateForCard(utc))
    }

    @Test
    fun `formattedDateForDivider handles single digit month`() {
        val movie = Movie(
            4, "Test Movie", "Description", false,
            Movie.toInstant("2021-05-15"), 4.5f, 100, "/image.jpg"
        )
        val expectedDate = "May 2021"
        assertEquals(expectedDate, movie.formattedDateForDivider(utc))
    }

    @Test
    fun `isAnotherMonthOrYear handles null input`() {
        assertEquals(true, movieWithImagePath.isAnotherMonthOrYear(null, utc))
    }

    @Test
    fun `imageUrl handles empty imagePath`() {
        val movieWithEmptyImagePath = movieWithImagePath.copy(imagePath = "")
        val expectedUrl = "https://picsum.photos/200/300"
        assertEquals(expectedUrl, movieWithEmptyImagePath.imageUrl)
    }

    @Test
    fun `toInstant handles invalid date format by returning a fallback Instant`() {
        val nowMillisBefore = Clock.System.now().toEpochMilliseconds()
        val fallback = Movie.toInstant("invalid-date")
        assertTrue(fallback.toEpochMilliseconds() >= nowMillisBefore)
    }

    @Test
    fun `toRaw handles epoch Instant`() {
        val epoch = kotlinx.datetime.Instant.fromEpochMilliseconds(0)
        assertEquals("1970-01-01", Movie.toRaw(epoch))
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
        val expectedTime = movieWithImagePath.date.toEpochMilliseconds()
        assertEquals(expectedTime, sameDateMovieList.findRecentTimeInMilli())
    }
}

private fun kotlinx.datetime.Instant.toLocalDateTimeUtc() =
    toLocalDateTime(TimeZone.UTC)
