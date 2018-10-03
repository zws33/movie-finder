package me.zwsmith.moviefinder.core.services

import android.util.Log
import com.google.gson.Gson
import me.zwsmith.moviefinder.core.models.MovieDetailsResponse
import me.zwsmith.moviefinder.core.models.MovieListResponse
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import javax.inject.Inject

class MovieServiceImpl @Inject constructor(
        private val okHttpClient: OkHttpClient,
        private val gson: Gson
) : MovieService {
    private val popularMoviesUrlBuilder = HttpUrl.Builder()
            .scheme("https")
            .host(BASE_URL)
            .addPathSegment(BASE_PATH)
            .addPathSegment(DISCOVER)
            .addPathSegment(MOVIE)
            .addQueryParameter("language", "en-US")
            .addQueryParameter("sort_by", "popularity.desc")

    private val movieDetailsUrlBuilder = HttpUrl.Builder()
            .scheme("https")
            .host(BASE_URL)
            .addPathSegment(BASE_PATH)
            .addPathSegment(MOVIE)

    override fun getPopularMovies(pageNumber: Int): MovieListResponse {

        val url: HttpUrl = popularMoviesUrlBuilder
                .addQueryParameter("page", pageNumber.toString())
                .build()
                .also { Log.d(TAG, "Popular movies url = $it") }

        return gson.fromJson(url.get().charStream(), MovieListResponse::class.java)
    }

    override fun getMovieDetailsById(id: String): MovieDetailsResponse {
        val url = movieDetailsUrlBuilder
                .addPathSegment(id)
                .build()
                .also { Log.d(TAG, "Movie details url = $it") }

        return gson.fromJson(url.get().charStream(), MovieDetailsResponse::class.java)
    }

    private fun HttpUrl.get(): ResponseBody {
        val request = Request.Builder().url(this).build()

        return okHttpClient.newCall(request).execute().body()
                ?: throw IllegalArgumentException("Response body was null")
    }

    companion object {
        private val TAG = MovieServiceImpl::class.java.simpleName
        private const val BASE_URL = "api.themoviedb.org"
        private const val BASE_PATH = "3"
        private const val DISCOVER = "discover"
        private const val MOVIE = "movie"

    }
}