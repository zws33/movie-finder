package me.zwsmith.moviefinder.core.repositories

import android.util.Log
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.ReplayRelay
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import me.zwsmith.moviefinder.core.common.ResponseStatus
import me.zwsmith.moviefinder.core.common.wrapResponse
import me.zwsmith.moviefinder.core.services.MovieDetailsResponse
import me.zwsmith.moviefinder.core.services.MovieService
import me.zwsmith.moviefinder.core.services.PopularMoviesResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(private val movieService: MovieService) {

    private val popularMoviesRelay = BehaviorRelay.create<ResponseStatus<PopularMoviesResponse>>()

    val popularMoviesStream: Observable<ResponseStatus<PopularMoviesResponse>> = popularMoviesRelay

    private var currentPopularPage = INITIAL_POPULAR_MOVIES_PAGE

    fun refreshPopularMovies() = getPopularMovies(currentPopularPage)

    private fun getPopularMovies(pageNumber: Int = 1) {
        movieService
                .getPopularMovies(pageNumber)
                .wrapResponse()
                .subscribeBy(
                        onSuccess = { popularMoviesResult ->
                            Log.i(TAG, popularMoviesResult.toString())
                            popularMoviesRelay.accept(popularMoviesResult)
                        },
                        onError = { e: Throwable ->
                            Log.e(TAG, "Error message: ${e.message}", e)
                        }
                )
    }

    fun loadNextPopularMoviesPage() {
        getPopularMovies(currentPopularPage++)
    }

    fun getMovieDetailsById(id: String): Single<MovieDetailsResponse> {
        return movieService.getMovieDetailsById(id)
    }

    companion object {
        private val TAG = MovieRepository::class.java.simpleName
        private const val INITIAL_POPULAR_MOVIES_PAGE = 1
    }
}