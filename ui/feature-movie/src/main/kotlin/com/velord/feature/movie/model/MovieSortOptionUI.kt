package com.velord.feature.movie.model

import com.velord.core.resource.Res
import com.velord.core.resource.sort_by_date_ascending
import com.velord.core.resource.sort_by_date_descending
import com.velord.model.movie.MovieSortOption
import com.velord.model.movie.SortType
import org.jetbrains.compose.resources.StringResource

data class MovieSortOptionUI(
    val type: SortType,
    val isSelected: Boolean,
    val name: StringResource
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
                SortType.DateAscending -> Res.string.sort_by_date_ascending
                SortType.DateDescending -> Res.string.sort_by_date_descending
            }
        )
    }
}
