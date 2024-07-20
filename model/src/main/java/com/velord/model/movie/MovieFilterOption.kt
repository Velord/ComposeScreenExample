package com.velord.model.movie

enum class FilterType {
    Title_10_Characters,
    Description_50_Characters,
}

data class MovieFilterOption(
    val type: FilterType,
    val isSelected: Boolean
)