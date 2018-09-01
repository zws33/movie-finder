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
    private fun getMovieDetailsViewState(movieId: String): Single<MovieDetailsState> {
        return getMovieDetailsSingle(movieId)
                .map { it.toMovieDetailsState() }

    }

    fun getMovieDetailsSingle(movieId: String): Single<ResponseStatus<MovieDetailsResponse>> {
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
}

data class MovieDetailsViewState(
        val isLoadingVisible: Boolean,
        val isErrorVisible: Boolean,
        val title: String,
        val overview: String?,
        val backdropPath: String?
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