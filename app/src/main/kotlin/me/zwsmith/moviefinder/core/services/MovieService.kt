package me.zwsmith.moviefinder.core.services

import me.zwsmith.moviefinder.core.models.MovieDetailsResponse
import me.zwsmith.moviefinder.core.models.PopularMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("movie/popular?language=en-US&sort_by=rating.desc")
    suspend fun getPopularMovies(@Query("page") pageNumber: Int): PopularMoviesResponse

    @GET("movie/{id}")
    suspend fun getMovieDetailsById(@Path("id") id: String): MovieDetailsResponse

    @GET("genre/movie/list")
    suspend fun getGenres(): GenresResponse

    companion object {
        private val TAG = MovieService::class.java.simpleName
    }
}

data class GenresResponse(val genres: List<Genre>)
data class Genre(
    val id: Int,
    val name: String
)