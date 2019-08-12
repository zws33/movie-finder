package me.zwsmith.moviefinder.presentation.movieDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.zwsmith.moviefinder.core.repositories.MovieRepository

class MovieDetailsViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _viewStates = MutableLiveData<MovieDetailsViewState>()
    val viewStates: LiveData<MovieDetailsViewState> = _viewStates

    fun loadMovieDetails(movieId: String) {
        viewModelScope.launch {
            val viewState: MovieDetailsViewState = withContext(Dispatchers.IO) {
                movieRepository.getMovieDetailsById(movieId).let { movieDetails ->
                    MovieDetailsViewState(isLoadingVisible = false,
                        isErrorVisible = false,
                        overview = movieDetails.overview,
                        title = movieDetails.title,
                        backdropUrl = movieDetails.backdropPath?.let { IMAGE_BASE_URL + it }
                    )
                }
            }
            _viewStates.value = viewState
        }
    }

    companion object {
        private val TAG = MovieDetailsViewModel::class.java.simpleName
        private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w1280"
    }
}

data class MovieDetailsViewState(
    val isLoadingVisible: Boolean,
    val isErrorVisible: Boolean,
    val title: String?,
    val overview: String?,
    val backdropUrl: String?
)