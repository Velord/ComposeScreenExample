package com.velord.backend.ktor

import com.velord.backend.model.ErrorResponse
import io.ktor.client.call.body
import io.ktor.client.request.setBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Serializable
data class MovieRequest(
    val id: Int,
)

@Serializable
data class MovieResponse(
    val id: Int,
)

@Serializable
data class MovieRosterResponse(
    @SerialName("movie")
    val movie: List<MovieResponse> = emptyList(),
    val error: ErrorResponse
)

interface MovieService {
    suspend fun getMovie(): MovieRosterResponse
}

@Factory
class MovieServiceImpl(
    @Provided private val client: BaseHttpClient
) : MovieService {

    override suspend fun getMovie(): MovieRosterResponse {
        val request = MovieRequest(345)
        return client.post { setBody(request) }.body()
    }
}
