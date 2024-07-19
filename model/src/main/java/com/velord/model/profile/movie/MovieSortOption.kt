package com.velord.model.profile.movie

enum class SortType {
    DateAscending,
    DateDescending
}

data class MovieSortOption(
    val type: SortType,
    val isSelected: Boolean
)