package me.zwsmith.moviefinder.presentation.movieResults

import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.Observable
import me.zwsmith.moviefinder.core.common.ResponseStatus
import me.zwsmith.moviefinder.core.interactors.GetPopularMoviesStreamInteractor
import me.zwsmith.moviefinder.core.interactors.RequestNextPopularMoviesPageInteractor
import me.zwsmith.moviefinder.core.services.PopularMoviesResponse
import javax.inject.Inject


class MovieResultsViewModel @Inject constructor(
        private val getPopularMoviesStreamInteractor: GetPopularMoviesStreamInteractor,
        private val requestNextPopularMoviesPageInteractor: RequestNextPopularMoviesPageInteractor
) : ViewModel() {

    val movieListViewStateStream: Observable<MovieResultsViewState> =
            getPopularMoviesStreamInteractor.popularMoviesStream
                    .map { responseStatus -> responseStatus.toMovieResultsState() }
                    .map { state -> state.toMovieResultsViewState() }
                    .doOnNext { Log.d(TAG, it.toString()) }

    fun loadNextPage() {
        requestNextPopularMoviesPageInteractor.requestNextPage()
    }

    private fun ResponseStatus<PopularMoviesResponse>.toMovieResultsState(): MovieResultsState {
        return when (this) {
            is ResponseStatus.Complete -> {
                when (this) {
                    is ResponseStatus.Complete.Success -> {
                        handleSuccess(this)
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
    }

    private fun MovieResultsState.toMovieResultsViewState(): MovieResultsViewState {
        return when (this) {
            is MovieResultsState.Success -> {
                MovieResultsViewState(
                        isLoadingVisible = false,
                        isErrorVisible = false,
                        movieResults = movieResults.map { movie -> movie.toMovieItemViewState() }
                )
            }
            MovieResultsState.Loading -> {
                MovieResultsViewState(
                        isLoadingVisible = true,
                        isErrorVisible = false,
                        movieResults = null
                )
            }
            MovieResultsState.Error -> {
                MovieResultsViewState(
                        isLoadingVisible = true,
                        isErrorVisible = true,
                        movieResults = null
                )
            }
        }
    }

    private fun Movie.toMovieItemViewState(): MovieItemViewState {
        return MovieItemViewState(
                id,
                title,
                genres.joinToString(", "),
                posterPath?.let { IMAGE_BASE_URL + it }
        )
    }

    companion object {
        private val TAG = MovieResultsViewModel::class.java.simpleName
        private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w45"
    }
}

data class MovieResultsViewState(
        val isLoadingVisible: Boolean,
        val isErrorVisible: Boolean,
        val movieResults: List<MovieItemViewState>?
)

data class MovieItemViewState(
        val id: String,
        val title: String,
        val genres: String,
        val imageUrl: String?
)