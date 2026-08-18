package com.velord.ui.feature.movie.model

import com.velord.core.resource.AppString
import com.velord.core.resource.AppStringResource
import com.velord.model.movie.MovieSortOption
import com.velord.model.movie.SortType

data class MovieSortOptionUI(
    val type: SortType,
    val isSelected: Boolean,
    val name: AppStringResource
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
                SortType.DateAscending -> AppString.sort_by_date_ascending
                SortType.DateDescending -> AppString.sort_by_date_descending
            }
        )
    }
}
