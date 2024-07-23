package com.velord.model

import com.velord.model.movie.MovieSortOption
import com.velord.model.movie.SortType
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MovieSortOptionTest {

    @Test
    fun `Default option should have DateDescending type`() {
        assertEquals(SortType.DateDescending, MovieSortOption.Default.type)
    }

    @Test
    fun `Default option should be selected`() {
        assertEquals(true, MovieSortOption.Default.isSelected)
    }

    @Test
    fun `can create MovieSortOption with DateAscending type and unselected`() {
        val option = MovieSortOption(SortType.DateAscending, false)
        assertEquals(SortType.DateAscending, option.type)
        assertEquals(false, option.isSelected)
    }

    @Test
    fun `can create MovieSortOption with DateDescending type and selected`() {
        val option = MovieSortOption(SortType.DateDescending, true)
        assertEquals(SortType.DateDescending, option.type)
        assertEquals(true, option.isSelected)
    }

    @Test
    fun `two MovieSortOptions with same properties should be equal`() {
        val option1 = MovieSortOption(SortType.DateAscending, true)
        val option2 = MovieSortOption(SortType.DateAscending, true)
        assertEquals(option1, option2)
    }
}