package me.zwsmith.moviefinder.presentation.movieBrowser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.zwsmith.moviefinder.core.models.extensions.MovieListItem
import me.zwsmith.moviefinder.core.repositories.MovieRepository
import me.zwsmith.moviefinder.core.services.Genre

class MovieBrowserViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _viewStates = MutableLiveData<MovieBrowserViewState>()
    val viewStates: LiveData<MovieBrowserViewState> = _viewStates

    private val _movieSelection = MutableLiveData<String>()
    val movieSelection: LiveData<String> = _movieSelection

    fun loadMovies() {
        viewModelScope.launch {
            val viewState = withContext(Dispatchers.Default) {
                val genres = movieRepository.getGenres()
                val popularMovies = movieRepository.getPopularMovies(1)
                MovieBrowserViewState(
                    isLoadingVisible = false,
                    isErrorVisible = false,
                    moviesList = popularMovies.map {
                        buildMovieRowViewState(it, genres)
                    }
                )
            }
            _viewStates.postValue(viewState)
        }
    }

    private fun buildMovieRowViewState(movieListItem: MovieListItem, genres: List<Genre>): MovieBrowserViewState.RowViewState {
        return MovieBrowserViewState.RowViewState(
            movieListItem.id,
            movieListItem.title,
            movieListItem.rating.toString(),
            movieListItem.genreIds.map { genreId -> genres.first { it.id == genreId }.name },
            movieListItem.posterPath?.let { IMAGE_BASE_URL + it },
            onClick = { _movieSelection.postValue(movieListItem.id) }
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
    val moviesList: List<RowViewState>?
) {
    data class RowViewState(
        val id: String,
        val title: String,
        val rating: String,
        val genres: List<String>,
        val imageUrl: String?,
        val onClick: () -> Unit
    )
}

