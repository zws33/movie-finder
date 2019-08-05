package me.zwsmith.moviefinder.core.services

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieServiceV1 {

    @GET("discover/movie?language=en-US&sort_by=popularity.desc")
    fun getPopularMovies(@Query("page") pageNumber: Int): Single<PopularMoviesResponse>

    @GET("movie/{id}")
    fun getMovieDetailsById(@Path("id") id: String): Single<MovieDetailsResponse>

    companion object {
        private val TAG = MovieServiceV1::class.java.simpleName
    }
}

interface MovieService {
    @GET("discover/movie?language=en-US&sort_by=popularity.desc")
    suspend fun getPopularMovies(@Query("page") pageNumber: Int): PopularMoviesResponse

    @GET("movie/{id}")
    suspend fun getMovieDetailsById(@Path("id") id: String): MovieDetailsResponse

    companion object {
        private val TAG = MovieServiceV1::class.java.simpleName
    }
}