package me.zwsmith.moviefinder.presentation.movieBrowser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.zwsmith.moviefinder.core.models.extensions.MovieListItem
import me.zwsmith.moviefinder.core.repositories.MovieRepository

class MovieBrowserViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _viewStates = MutableLiveData<MovieBrowserViewState>()
    val viewStates: LiveData<MovieBrowserViewState> = _viewStates

    private val _movieSelection = MutableLiveData<String>()
    val movieSelection: LiveData<String> = _movieSelection

    fun loadMovies() {
        viewModelScope.launch {
            try {
                val viewState = withContext(Dispatchers.IO) {
                    MovieBrowserViewState(
                        isLoadingVisible = false,
                        isErrorVisible = false,
                        moviesList = movieRepository.getPopularMovies(1)
                            .map {
                                Log.d(TAG, it.toString())
                                it.toMovieRowViewState()
                            }
                    )
                }
                _viewStates.postValue(viewState)
            } catch (exception: Exception) {
                throw exception
            }
        }
    }

    private fun MovieListItem.toMovieRowViewState(): MovieBrowserViewState.RowViewState {
        return MovieBrowserViewState.RowViewState(
            id,
            title,
            popularity.toString(),
            posterPath?.let { IMAGE_BASE_URL + it },
            onClick = { _movieSelection.postValue(id) }
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
        val popularity: String,
        val imageUrl: String?,
        val onClick: () -> Unit
    )
}

