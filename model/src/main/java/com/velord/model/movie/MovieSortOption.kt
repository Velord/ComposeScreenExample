package com.velord.model.movie

enum class SortType {
    DateAscending,
    DateDescending
}

data class MovieSortOption(
    val type: SortType,
    val isSelected: Boolean
)