package com.velord.bd

import com.velord.model.movie.Movie
import org.koin.core.annotation.Single
import java.util.Calendar

interface MoviePersistentStorageService {
    suspend fun getMovie(page: Int): Movie
}

@Single
class MoviePersistentStorageServiceImpl : MoviePersistentStorageService {
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