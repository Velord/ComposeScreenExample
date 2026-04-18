package com.velord.usecase.movie

import com.velord.model.movie.MovieSortOption

fun interface SetMovieSortOptionUC : (MovieSortOption) -> Unit
