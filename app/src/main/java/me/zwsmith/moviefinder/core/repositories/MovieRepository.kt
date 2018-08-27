package me.zwsmith.moviefinder.core.repositories

import android.util.Log
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import me.zwsmith.moviefinder.core.common.Result
import me.zwsmith.moviefinder.core.common.wrapInResult
import me.zwsmith.moviefinder.core.services.MovieDetailsResponse
import me.zwsmith.moviefinder.core.services.MovieService
import me.zwsmith.moviefinder.core.services.PopularMoviesResponse
import javax.inject.Inject

class MovieRepository @Inject constructor(private val movieService: MovieService) {
    fun refreshPopularMovies() {
        movieService
                .getPopularMovies()
                .wrapInResult()
                .subscribeBy(
                        onSuccess = { popularMoviesResult ->
                            popularMoviesRelay.accept(popularMoviesResult)
                        },
                        onError = { e: Throwable ->
                            Log.e(TAG, "Error message: ${e.message}", e)
                        }
                )
    }

    private val popularMoviesRelay = PublishRelay.create<Result<PopularMoviesResponse>>()

    val popularMoviesStream: Observable<Result<PopularMoviesResponse>> by lazy {
        refreshPopularMovies()
        popularMoviesRelay
    }

    fun getMovieDetailsById(id: String): Single<MovieDetailsResponse> {
        return movieService.getMovieDetailsById(id)
    }

    companion object {
        private val TAG = MovieRepository::class.java.simpleName
    }
}