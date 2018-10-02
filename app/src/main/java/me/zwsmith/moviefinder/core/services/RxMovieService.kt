package me.zwsmith.moviefinder.core.services

import com.google.gson.Gson
import io.reactivex.Single
import me.zwsmith.moviefinder.core.models.MovieDetailsResponse
import me.zwsmith.moviefinder.core.models.MovieResultsResponse
import okhttp3.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.IOException

interface RxMovieService {
    @GET("discover/movie?language=en-US&sort_by=popularity.desc")
    fun getPopularMovies(@Query("page") pageNumber: Int): Single<MovieResultsResponse>

    @GET("movie/{id}")
    fun getMovieDetailsById(@Path("id") id: String): Single<MovieDetailsResponse>

    companion object {
        private val TAG = RxMovieService::class.java.simpleName
    }
}

interface MovieService {
    @GET("discover/movie?language=en-US&sort_by=popularity.desc")
    fun getPopularMovies(@Query("page") pageNumber: Int): MovieResultsResponse

    @GET("movie/{id}")
    fun getMovieDetailsById(@Path("id") id: String): MovieDetailsResponse

    companion object {
        private val TAG = RxMovieService::class.java.simpleName
    }
}

class MovieSericeImpl(
        private val okHttpClient: OkHttpClient,
        private val gson: Gson
) : MovieService {
    override fun getPopularMovies(pageNumber: Int): MovieResultsResponse {
        var movieResultsResponse: MovieResultsResponse? = null

        val urlBuilder = HttpUrl.parse(BASE_URL + DISCOVER_MOVIES_PATH)
                ?: throw RuntimeException("Unable to parse url")

        val url = urlBuilder.newBuilder()
                .addQueryParameter("language", "en-US")
                .addQueryParameter("sort_by", "popularity.dec")
                .build()

        val request = Request.Builder()
                .url(url)
                .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call, response: Response) {
                movieResultsResponse = gson.fromJson(
                        response.body()?.charStream(),
                        MovieResultsResponse::class.java
                )
            }
        })

        return movieResultsResponse ?: throw RuntimeException("Could not get response")
    }

    override fun getMovieDetailsById(id: String): MovieDetailsResponse {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private val TAG = MovieSericeImpl::class.java.simpleName
        private const val BASE_URL = "https://api.themoviedb.org/3/"
        private const val DISCOVER_MOVIES_PATH = "discover/movie"

    }
}