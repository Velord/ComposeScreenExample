package com.velord.backend.ktor

import com.velord.model.movie.Movie
import io.ktor.client.call.body
import io.ktor.http.path
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Serializable
data class MovieResponse(
    val id: Int,
    @SerialName("original_title")
    val title: String,
    @SerialName("release_date")
    val date: String,
    @SerialName("poster_path")
    val imagePath: String,
    @SerialName("overview")
    val description: String
) {
    private fun parseDate(): Calendar {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date: Date? = sdf.parse(date)
        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date
        }

        return calendar
    }

    fun toDomain() = Movie(
        id = id,
        title = title,
        description = description,
        isLiked = false,
        date = parseDate(),
    )
}

@Serializable
data class MovieRosterResponse(
    val page: Int,
    val results: List<MovieResponse>,
)

interface MovieService {
    suspend fun getMovie(page: MoviePageRequest): MovieRosterResponse
}

@JvmInline
value class MoviePageRequest(val page: Int)

@Single
class MovieServiceImpl(
    @Provided private val client: BaseHttpClient
) : MovieService {

    override suspend fun getMovie(page: MoviePageRequest): MovieRosterResponse {
        val sdf = "3/discover/movie"
        return client.get {
            url {
                path(sdf)
                parameters.append("vote_average.gte", "7")
                parameters.append("vote_average.lte", "7.1")
                parameters.append("vote_count.gte", "100")
                parameters.append("vote_count.lte", "200")
                parameters.append("sort_by", "primary_release_date.desc")
                parameters.append("sort_by", "primary_release_date.desc")
                parameters.append("page", page.page.toString())
            }
        }.body()
    }
}
