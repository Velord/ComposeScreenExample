package com.velord.model.movie

@JvmInline
value class MovieRosterSize(val value: Int) {
    init {
        require(value >= 0) { "MovieRosterSize must be greater than or equal to 0" }
    }
}