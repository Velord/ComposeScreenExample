package com.velord.feature.movie.model

import androidx.annotation.StringRes
import com.velord.model.movie.MovieSortOption
import com.velord.model.movie.SortType
import com.velord.resource.R

data class MovieSortOptionUI(
    val type: SortType,
    val isSelected: Boolean,
    @StringRes val name: Int
) {

    fun toDomain(): MovieSortOption = MovieSortOption(
        type = type,
        isSelected = isSelected
    )

    companion object {

        fun fromDomain(option: MovieSortOption): MovieSortOptionUI = MovieSortOptionUI(
            type = option.type,
            isSelected = option.isSelected,
            name = when (option.type) {
                SortType.DateAscending -> R.string.sort_by_date_ascending
                SortType.DateDescending -> R.string.sort_by_date_descending
            }
        )
    }
}