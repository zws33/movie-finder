package me.zwsmith.moviefinder.presentation.movieBrowser

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import me.zwsmith.moviefinder.core.common.ResponseStatus
import me.zwsmith.moviefinder.core.interactors.GetPopularMoviesInteractor
import me.zwsmith.moviefinder.core.interactors.RefreshPopularMoviesInteractor
import me.zwsmith.moviefinder.core.interactors.RequestNextPopularMoviesPageInteractor
import me.zwsmith.moviefinder.core.services.PopularMoviesResponse
import javax.inject.Inject


class MovieBrowserViewModel @Inject constructor(
        private val refreshPopularMoviesInteractor: RefreshPopularMoviesInteractor,
        private val getPopularMoviesInteractor: GetPopularMoviesInteractor,
        private val requestNextPopularMoviesPageInteractor: RequestNextPopularMoviesPageInteractor
) : ViewModel() {

    val viewStateStream: Observable<MovieBrowserViewState> =
            getPopularMoviesInteractor.popularMoviesStream
                    .map { responseStatus -> responseStatus.toMovieResultsState() }
                    .map { state -> state.toMovieBrowserViewState() }
                    .doOnSubscribe { refreshPopularMovies() }

    fun loadNextPage() {
        requestNextPopularMoviesPageInteractor.requestNextPage()
    }

    private fun refreshPopularMovies() {
        refreshPopularMoviesInteractor.refreshPopularMovies()
    }

    private fun ResponseStatus<PopularMoviesResponse>.toMovieResultsState(): MovieBrowserState {
        return when (this) {
            is ResponseStatus.Complete -> {
                when (this) {
                    is ResponseStatus.Complete.Success -> {
                        handleSuccess(this)
                    }
                    is ResponseStatus.Complete.Error -> {
                        MovieBrowserState.Error
                    }
                }
            }
            ResponseStatus.Pending -> {
                MovieBrowserState.Loading
            }
        }
    }

    private fun handleSuccess(
            responseStatus: ResponseStatus.Complete.Success<PopularMoviesResponse>
    ): MovieBrowserState.Success {
        val response = responseStatus.value
        val movieList = response.popularMovies.map { popularMovie ->
            MovieBrowserItem(
                    popularMovie.id.toString(),
                    popularMovie.title,
                    popularMovie.popularity,
                    popularMovie.posterPath
            )
        }
        return MovieBrowserState.Success(movieList)
    }

    private fun MovieBrowserState.toMovieBrowserViewState(): MovieBrowserViewState {
        return when (this) {
            is MovieBrowserState.Success -> {
                MovieBrowserViewState(
                        isLoadingVisible = false,
                        isErrorVisible = false,
                        movieResults = movies.map { movie -> movie.toMovieItemViewState() }
                )
            }
            MovieBrowserState.Loading -> {
                MovieBrowserViewState(
                        isLoadingVisible = true,
                        isErrorVisible = false,
                        movieResults = null
                )
            }
            MovieBrowserState.Error -> {
                MovieBrowserViewState(
                        isLoadingVisible = false,
                        isErrorVisible = true,
                        movieResults = null
                )
            }
        }
    }

    private fun MovieBrowserItem.toMovieItemViewState(): MovieItemViewState {
        return MovieItemViewState(
                id,
                title,
                popularity.toString(),
                posterPath?.let { IMAGE_BASE_URL + it }
        )
    }

    companion object {
        private val TAG = MovieBrowserViewModel::class.java.simpleName
        private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w45"
    }
}

data class MovieBrowserViewState(
        val isLoadingVisible: Boolean,
        val isErrorVisible: Boolean,
        val movieResults: List<MovieItemViewState>?
)

data class MovieItemViewState(
        val id: String,
        val title: String,
        val popularity: String,
        val imageUrl: String?
)