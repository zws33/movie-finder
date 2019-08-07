package me.zwsmith.moviefinder.core.services

import me.zwsmith.moviefinder.core.models.MovieDetailsResponse
import me.zwsmith.moviefinder.core.models.PopularMoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("discover/movie?language=en-US&sort_by=popularity.desc")
    suspend fun getPopularMovies(@Query("page") pageNumber: Int): Response<PopularMoviesResponse>

    @GET("movie/{id}")
    suspend fun getMovieDetailsById(@Path("id") id: String): Response<MovieDetailsResponse>

    companion object {
        private val TAG = MovieService::class.java.simpleName
    }
}