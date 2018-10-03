package me.zwsmith.moviefinder.core.services

import io.reactivex.Single
import me.zwsmith.moviefinder.core.models.MovieDetailsResponse
import me.zwsmith.moviefinder.core.models.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RxMovieService {
    @GET("discover/movie?language=en-US&sort_by=popularity.desc")
    fun getPopularMovies(@Query("page") pageNumber: Int): Single<MovieListResponse>

    @GET("movie/{id}")
    fun getMovieDetailsById(@Path("id") id: String): Single<MovieDetailsResponse>

    companion object {
        private val TAG = RxMovieService::class.java.simpleName
    }
}