package me.zwsmith.moviefinder.presentation.movieResults

import io.reactivex.Completable
import io.reactivex.Observable
import me.zwsmith.moviefinder.core.services.PopularMoviesResponse

sealed class MovieResultsState {
    object Loading : MovieResultsState()
    object Error : MovieResultsState()
    data class Success(
            val movieResults: List<Movie>
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
        refreshPopularMoviesCompletable: Completable,
        movieResultsStream: Observable<PopularMoviesResponse>
): Observable<MovieResultsState> {

    val initialStateStream: Observable<MovieResultsState> = getInitialStateStream(movieResultsStream)

    val intentReducerStream: Observable<StateReducer> by lazy { TODO() }

    return initialStateStream.switchMap { initialState ->
        intentReducerStream.scan(initialState) { oldState: MovieResultsState, reducer: StateReducer ->
            reducer(oldState)
        }
    }
}

fun getInitialStateStream(
        movieResultsStream: Observable<PopularMoviesResponse>
): Observable<MovieResultsState> {
    return movieResultsStream.map { response: PopularMoviesResponse ->
        val movieList = response.popularMovies.map { popularMovie ->
            Movie(
                    popularMovie.id.toString(),
                    popularMovie.title,
                    popularMovie.genreIds.map { it.toString() },
                    popularMovie.posterPath
            )
        }
        MovieResultsState.Success(movieList)
    }
}

private typealias StateReducer = (MovieResultsState) -> MovieResultsState