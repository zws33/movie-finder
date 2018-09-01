package me.zwsmith.moviefinder.presentation.movieDetails

import android.arch.lifecycle.ViewModel
import io.reactivex.Single
import me.zwsmith.moviefinder.core.interactors.GetMovieDetailsInteractor
import me.zwsmith.moviefinder.core.services.MovieDetailsResponse
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(
        private val getMovieDetailsInteractor: GetMovieDetailsInteractor
) : ViewModel() {
    fun getMovieDetailsSingle(movieId: String): Single<MovieDetailsResponse> {
        return getMovieDetailsInteractor.getMovieDetailsById(movieId)
    }
}