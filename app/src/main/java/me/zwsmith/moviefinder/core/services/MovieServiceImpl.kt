package me.zwsmith.moviefinder.core.services

import com.google.gson.Gson
import me.zwsmith.moviefinder.core.common.Logger
import me.zwsmith.moviefinder.core.models.MovieDetailsResponse
import me.zwsmith.moviefinder.core.models.MovieListResponse
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class MovieServiceImpl @Inject constructor(
        private val okHttpClient: OkHttpClient,
        private val gson: Gson,
        private val logger: Logger
) : MovieService {
    private val popularMoviesUrlBuilder = HttpUrl.Builder()
            .scheme("https")
            .host(HOST)
            .addPathSegment(BASE_PATH)
            .addPathSegment(DISCOVER)
            .addPathSegment(MOVIE)
            .addQueryParameter("language", "en-US")
            .addQueryParameter("sort_by", "popularity.desc")

    private val movieDetailsUrlBuilder = HttpUrl.Builder()
            .scheme("https")
            .host(HOST)
            .addPathSegment(BASE_PATH)
            .addPathSegment(MOVIE)

    override fun getPopularMovies(pageNumber: Int): MovieListResponse {

        val url: HttpUrl = popularMoviesUrlBuilder
                .addQueryParameter("page", pageNumber.toString())
                .build()
                .also { logger.d(TAG, "Popular movies url = $it") }

        val request = Request.Builder().url(url).build()

        val responseBody = okHttpClient.newCall(request).execute().body()
                ?: throw IllegalArgumentException("Response body was null")

        return gson.fromJson(responseBody.charStream(), MovieListResponse::class.java)
    }

    override fun getMovieDetailsById(id: String): MovieDetailsResponse {
        val url = movieDetailsUrlBuilder
                .addPathSegment(id)
                .build()
                .also { logger.d(TAG, "Movie details url = $it") }

        val request = Request.Builder().url(url).build()

        val responseBody = okHttpClient.newCall(request).execute().body()
                ?: throw IllegalArgumentException("Response body was null")

        return gson.fromJson(responseBody.charStream(), MovieDetailsResponse::class.java)
    }

    companion object {
        private val TAG = MovieServiceImpl::class.java.simpleName
        private const val HOST = "api.themoviedb.org"
        private const val BASE_PATH = "3"
        private const val DISCOVER = "discover"
        private const val MOVIE = "movie"

    }
}