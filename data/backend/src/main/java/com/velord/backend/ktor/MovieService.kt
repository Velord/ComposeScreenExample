package com.velord.backend.ktor

import com.velord.backend.model.MoviePageRequest
import com.velord.backend.model.MovieRosterResponse
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.http.path
import kotlinx.coroutines.delay
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

interface MovieService {
    suspend fun getMovie(page: MoviePageRequest): MovieRosterResponse
}

private const val AUTHORIZATION_HEADER = "Authorization"
private const val BEARER = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjYjNiOGJhNDk4ZGFiOTUzYmZhYzVhMTI4YzQ0ZWM2ZSIsIm5iZiI6MTcyMTMzNTMzOC4yMDgxMzYsInN1YiI6IjY2OTk3ZDE0OTU3YjM2NWNjOGZkNzIwOCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.VoVoq1XiABNy-3i8BUe-WXvg7Sp5AjDfrF-yFMmh1eM"

@Single
class MovieServiceImpl(
    @Provided private val client: BaseHttpClient
) : MovieService {

    override suspend fun getMovie(page: MoviePageRequest): MovieRosterResponse {
        val sdf = "/3/discover/movie"
        delay(300)
        return client.get {
            header(AUTHORIZATION_HEADER, BEARER)
            url {
                path(sdf)
                parameters.append("vote_average.gte", "7")
                parameters.append("vote_average.lte", "7.1")
                parameters.append("vote_count.gte", "5000")
                parameters.append("vote_count.lte", "6000")
                parameters.append("sort_by", "primary_release_date.desc")
                parameters.append("sort_by", "primary_release_date.desc")
                parameters.append("page", page.page.toString())
            }
        }.body()
    }
}