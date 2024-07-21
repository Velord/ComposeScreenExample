package com.velord.db

import com.velord.model.movie.Movie
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single
import java.util.Calendar

interface MovieDbService {
    suspend fun getMovie(page: Int): Movie
}

@Single
class MovieDbServiceImpl(
    @Provided private val db: MovieDao
) : MovieDbService {

    override suspend fun getMovie(page: Int): Movie {
        return Movie(
            id = 23,
            title = "Description",
            description = "2021",
            isLiked = false,
            date = Calendar.getInstance(),
            imagePath = null,
            rating = 5.0f
        )
    }
}