package me.zwsmith.moviefinder.core.repositories

import android.util.Log
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import me.zwsmith.moviefinder.core.common.ResponseStatus
import me.zwsmith.moviefinder.core.common.wrapResponse
import me.zwsmith.moviefinder.core.models.MovieDetailsResponse
import me.zwsmith.moviefinder.core.models.MovieResultsResponse
import me.zwsmith.moviefinder.core.services.RxMovieService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
        private val movieService: RxMovieService
) : MovieRepository {

    private val popularMoviesRelay = BehaviorRelay.create<ResponseStatus<MovieResultsResponse>>()

    override val popularMoviesStream: Observable<ResponseStatus<MovieResultsResponse>> = popularMoviesRelay

    private var currentPopularPage = INITIAL_POPULAR_MOVIES_PAGE

    override fun refreshPopularMovies() {
        currentPopularPage = INITIAL_POPULAR_MOVIES_PAGE
        popularMoviesRelay.accept(ResponseStatus.Pending)
        getPopularMovies(currentPopularPage)
    }

    private fun getPopularMovies(pageNumber: Int = INITIAL_POPULAR_MOVIES_PAGE) {
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

    override fun loadNextPopularMoviesPage() {
        currentPopularPage++
        getPopularMovies(currentPopularPage)
    }

    override fun getMovieDetailsById(id: String): Single<ResponseStatus<MovieDetailsResponse>> {
        return movieService.getMovieDetailsById(id).wrapResponse()
    }

    companion object {
        private val TAG = MovieRepositoryImpl::class.java.simpleName
        private const val INITIAL_POPULAR_MOVIES_PAGE = 1
    }
}

interface MovieRepository {
    val popularMoviesStream: Observable<ResponseStatus<MovieResultsResponse>>
    fun refreshPopularMovies()
    fun loadNextPopularMoviesPage()
    fun getMovieDetailsById(id: String): Single<ResponseStatus<MovieDetailsResponse>>
}