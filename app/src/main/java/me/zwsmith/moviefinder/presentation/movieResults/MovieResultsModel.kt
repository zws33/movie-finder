package me.zwsmith.moviefinder.presentation.movieResults

import io.reactivex.Completable
import io.reactivex.Observable
import me.zwsmith.moviefinder.core.common.Result
import me.zwsmith.moviefinder.core.extensions.just
import me.zwsmith.moviefinder.core.services.PopularMoviesResponse

sealed class MovieResultsState {
    object Loading : MovieResultsState()
    object Error : MovieResultsState()
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
        movieResultsStream: Observable<Result<PopularMoviesResponse>>
): Observable<MovieResultsState> {

    val initialStateStream: Observable<MovieResultsState> = getInitialStateStream(movieResultsStream)

    val intentReducerStream: Observable<StateReducer> =
            intentStream.flatMap { intent: MovieResultsIntent ->
                getStateReducerForIntent(
                        intent,
                        refreshPopularMovies,
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

fun getStateReducerForIntent(
        intent: MovieResultsIntent,
        refreshPopularMovies: Completable,
        movieResultsStream: Observable<Result<PopularMoviesResponse>>
): Observable<StateReducer> {
    return when (intent) {
        is MovieResultsIntent.NavigateToMovieDetails -> {
            { oldState: MovieResultsState ->
                MovieResultsState.NavigateToMovieDetails(intent.id)
            }.just()
        }
        MovieResultsIntent.RefreshPopularMovies -> {
            refreshPopularMovies
                    .andThen(movieResultsStream)
                    .flatMap { result ->
                        when (result) {
                            is Result.Complete -> {
                                when (result) {
                                    is Result.Complete.Success -> {
                                        { oldState: MovieResultsState -> handleSuccess(result) }
                                    }
                                    is Result.Complete.Error -> {
                                        { oldState: MovieResultsState -> MovieResultsState.Error }
                                    }
                                }
                            }
                            Result.Pending -> {
                                { MovieResultsState.Loading }
                            }
                        }.just()
                    }
        }
    }
}

fun getInitialStateStream(
        movieResultsStream: Observable<Result<PopularMoviesResponse>>
): Observable<MovieResultsState> {
    return movieResultsStream.map { result ->
        when (result) {
            is Result.Complete -> {
                when (result) {
                    is Result.Complete.Success -> {
                        handleSuccess(result)
                    }
                    is Result.Complete.Error -> {
                        MovieResultsState.Error
                    }
                }
            }
            Result.Pending -> {
                MovieResultsState.Loading
            }
        }
    }
}

fun handleSuccess(result: Result.Complete.Success<PopularMoviesResponse>): MovieResultsState.Success {
    val response = result.value
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