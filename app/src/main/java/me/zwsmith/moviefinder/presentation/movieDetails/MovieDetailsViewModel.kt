package me.zwsmith.moviefinder.presentation.movieDetails

import android.arch.lifecycle.ViewModel
import io.reactivex.Single
import me.zwsmith.moviefinder.core.common.ResponseStatus
import me.zwsmith.moviefinder.core.interactors.GetMovieDetailsInteractor
import me.zwsmith.moviefinder.core.services.MovieDetailsResponse
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(
        private val getMovieDetailsInteractor: GetMovieDetailsInteractor
) : ViewModel() {

    fun getMovieDetailsViewState(movieId: String): Single<MovieDetailsViewState> {
        return getMovieDetailsSingle(movieId)
                .map { responseStatus -> responseStatus.toMovieDetailsState() }
                .map { state -> state.toMovieDetailsViewState() }
    }

    private fun MovieDetailsState.toMovieDetailsViewState(): MovieDetailsViewState {
        return when (this) {
            is MovieDetailsState.Success -> {
                MovieDetailsViewState(
                        isLoadingVisible = false,
                        isErrorVisible = false,
                        title = movieDetails.title,
                        overview = movieDetails.overview,
                        backdropUrl = movieDetails.backdropPath?.let { IMAGE_BASE_URL + it }
                )
            }
            MovieDetailsState.Loading -> {
                MovieDetailsViewState(
                        isLoadingVisible = true,
                        isErrorVisible = false,
                        title = null,
                        overview = null,
                        backdropUrl = null
                )
            }
            MovieDetailsState.Error -> {
                MovieDetailsViewState(
                        isLoadingVisible = true,
                        isErrorVisible = true,
                        title = null,
                        overview = null,
                        backdropUrl = null
                )
            }
        }
    }

    private fun getMovieDetailsSingle(
            movieId: String
    ): Single<ResponseStatus<MovieDetailsResponse>> {
        return getMovieDetailsInteractor.getMovieDetailsById(movieId)
    }

    private fun ResponseStatus<MovieDetailsResponse>.toMovieDetailsState(): MovieDetailsState {
        return when (this) {
            is ResponseStatus.Complete -> {
                when (this) {
                    is ResponseStatus.Complete.Success -> {
                        handleSuccess(this)
                    }
                    is ResponseStatus.Complete.Error -> {
                        MovieDetailsState.Error
                    }
                }
            }
            ResponseStatus.Pending -> {
                MovieDetailsState.Loading
            }
        }
    }

    private fun handleSuccess(
            responseStatus: ResponseStatus.Complete.Success<MovieDetailsResponse>
    ): MovieDetailsState.Success {
        val response = responseStatus.value
        val movieDetails = MovieDetails(
                response.backdropPath,
                response.title,
                response.overview
        )

        return MovieDetailsState.Success(movieDetails)
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

sealed class MovieDetailsState {
    object Loading : MovieDetailsState() {
        override fun toString(): String = Loading::class.java.simpleName
    }

    object Error : MovieDetailsState() {
        override fun toString(): String = Loading::class.java.simpleName
    }

    data class Success(
            val movieDetails: MovieDetails
    ) : MovieDetailsState()
}

data class MovieDetails(
        val backdropPath: String?,
        val title: String,
        val overview: String?
)