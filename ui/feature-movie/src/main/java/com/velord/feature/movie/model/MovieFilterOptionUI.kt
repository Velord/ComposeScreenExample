package com.velord.feature.movie.model

import androidx.annotation.StringRes
import com.velord.model.movie.FilterType
import com.velord.model.movie.MovieFilterOption
import com.velord.resource.R

data class MovieFilterOptionUI(
    val type: FilterType,
    val isSelected: Boolean,
    @StringRes val name: Int
) {
    companion object {
        fun fromDomain(option: MovieFilterOption): MovieFilterOptionUI = MovieFilterOptionUI(
            type = option.type,
            isSelected = option.isSelected,
            name = when (option.type) {
                FilterType.Title_10_Characters -> R.string.filter_by_title_over_10_char
                FilterType.Description_50_Characters -> R.string.filter_by_description_over_50_char
            }
        )
    }
}