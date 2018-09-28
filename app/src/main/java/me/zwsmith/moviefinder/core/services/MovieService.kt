package me.zwsmith.moviefinder.core.services

import io.reactivex.Single
import me.zwsmith.moviefinder.core.models.MovieDetailsResponse
import me.zwsmith.moviefinder.core.models.MovieResultsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("discover/movie?language=en-US&sort_by=popularity.desc")
    fun getPopularMovies(@Query("page") pageNumber: Int): Single<MovieResultsResponse>

    @GET("movie/{id}")
    fun getMovieDetailsById(@Path("id") id: String): Single<MovieDetailsResponse>

    companion object {
        private val TAG = MovieService::class.java.simpleName
    }
}