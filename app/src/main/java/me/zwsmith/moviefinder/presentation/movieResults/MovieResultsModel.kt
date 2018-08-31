package me.zwsmith.moviefinder.presentation.movieResults

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import me.zwsmith.moviefinder.core.common.ResponseStatus
import me.zwsmith.moviefinder.core.extensions.just
import me.zwsmith.moviefinder.core.services.PopularMoviesResponse

sealed class MovieResultsState {
    object Loading : MovieResultsState() {
        override fun toString(): String = Loading::class.java.simpleName
    }

    object Error : MovieResultsState() {
        override fun toString(): String = Loading::class.java.simpleName
    }

    data class Success(
            val movieResults: List<Movie>
    ) : MovieResultsState()

    data class NavigateToMovieDetails(
            val movieId: String
    ) : MovieResultsState()
}

data class Movie(
        val id: String,
        val title: String,
        val genres: List<String>,
        val posterPath: String?
)

fun buildMovieResultsStateStream(
        intentStream: Observable<MovieResultsIntent>,
        refreshPopularMovies: Completable,
        loadNextPopularMoviesPage: Completable,
        movieResultsStream: Observable<ResponseStatus<PopularMoviesResponse>>
): Observable<MovieResultsState> {

    val initialStateStream: Observable<MovieResultsState> =
            getInitialStateStream(movieResultsStream)
                    .doOnComplete { Log.d("Model", "Initial state stream completed") }

    val intentReducerStream: Observable<StateReducer> =
            intentStream
                    .doOnNext { Log.d("Model", it.toString()) }
                    .flatMap { intent: MovieResultsIntent ->
                        getStateReducerStream(
                                intent,
                                refreshPopularMovies,
                                loadNextPopularMoviesPage,
                                movieResultsStream
                        )
                    }


    return initialStateStream.switchMap { initialState ->
        intentReducerStream
                .scan(initialState) { oldState: MovieResultsState, reducer: StateReducer ->
                    reducer(oldState)
                }
    }
}

private fun getStateReducerStream(
        intent: MovieResultsIntent,
        refreshPopularMovies: Completable,
        loadNextPopularMoviesPage: Completable,
        movieResultsStream: Observable<ResponseStatus<PopularMoviesResponse>>
): Observable<StateReducer> {
    return when (intent) {
        MovieResultsIntent.RefreshPopularMovies -> {
            refreshPopularMovies
                    .andThen(movieResultsStream)
                    .flatMap {
                        getStateReducerStreamForResult(it)
                    }
        }
        MovieResultsIntent.LoadNextPopularMoviesPage -> {
            Log.d("Model", "Handling on load next page intent.")
            loadNextPopularMoviesPage
                    .andThen(movieResultsStream)
                    .flatMap {
                        getStateReducerStreamForResult(it)
                    }
        }
    }
}

private fun getStateReducerStreamForResult(
        result: ResponseStatus<PopularMoviesResponse>
): Observable<StateReducer> {
    return when (result) {
        is ResponseStatus.Complete -> {
            when (result) {
                is ResponseStatus.Complete.Success -> {
                    { _: MovieResultsState -> handleSuccess(result) }
                }
                is ResponseStatus.Complete.Error -> {
                    { _: MovieResultsState -> MovieResultsState.Error }
                }
            }
        }
        ResponseStatus.Pending -> {
            { MovieResultsState.Loading }
        }
    }.just()
}

private fun getInitialStateStream(
        movieResultsStream: Observable<ResponseStatus<PopularMoviesResponse>>
): Observable<MovieResultsState> {

    return movieResultsStream.map { result ->
        when (result) {
            is ResponseStatus.Complete -> {
                when (result) {
                    is ResponseStatus.Complete.Success -> {
                        handleSuccess(result)
                    }
                    is ResponseStatus.Complete.Error -> {
                        MovieResultsState.Error
                    }
                }
            }
            ResponseStatus.Pending -> {
                MovieResultsState.Loading
            }
        }
    }.startWith(MovieResultsState.Loading)
}

fun handleSuccess(
        responseStatus: ResponseStatus.Complete.Success<PopularMoviesResponse>
): MovieResultsState.Success {
    val response = responseStatus.value
    val movieList = response.popularMovies.map { popularMovie ->
        Movie(
                popularMovie.id.toString(),
                popularMovie.title,
                popularMovie.genreIds.map { it.toString() },
                popularMovie.posterPath
        )
    }
    return MovieResultsState.Success(movieList)
}

private typealias StateReducer = (MovieResultsState) -> MovieResultsState