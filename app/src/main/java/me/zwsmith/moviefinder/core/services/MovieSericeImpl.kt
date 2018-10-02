package me.zwsmith.moviefinder.core.services

import android.util.Log
import com.google.gson.Gson
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Single
import me.zwsmith.moviefinder.core.models.MovieDetailsResponse
import me.zwsmith.moviefinder.core.models.MovieResultsResponse
import okhttp3.*
import java.io.IOException
import javax.inject.Inject

class MovieSericeImpl @Inject constructor(
        private val okHttpClient: OkHttpClient,
        private val gson: Gson
) : RxMovieService {
    private val popularMoviesUrlBuilder = HttpUrl.Builder()
            .scheme("https")
            .host(BASE_URL)
            .addEncodedPathSegments(DISCOVER_MOVIES_PATH)
            .addQueryParameter("language", "en-US")
            .addQueryParameter("sort_by", "popularity.desc")

    override fun getPopularMovies(pageNumber: Int): Single<MovieResultsResponse> {
        val popularMovieRelay = BehaviorRelay.create<MovieResultsResponse>()

        val url = popularMoviesUrlBuilder
                .addQueryParameter("page", pageNumber.toString())
                .build()

        Log.d(TAG, url.toString())

        val request = Request.Builder()
                .url(url)
                .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call, response: Response) {
                val movieResultsResponse = gson.fromJson(
                        response.body()?.charStream(),
                        MovieResultsResponse::class.java
                )
                Log.d(TAG, movieResultsResponse.toString())
                popularMovieRelay.accept(movieResultsResponse)
            }
        })

        return popularMovieRelay.firstOrError()
    }

    override fun getMovieDetailsById(id: String): Single<MovieDetailsResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private val TAG = MovieSericeImpl::class.java.simpleName
        private const val BASE_URL = "api.themoviedb.org"
        private const val DISCOVER_MOVIES_PATH = "/3/discover/movie"

    }
}