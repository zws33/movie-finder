package me.zwsmith.moviefinder.core.interactors

import io.reactivex.Observable
import me.zwsmith.moviefinder.core.common.ResponseStatus
import me.zwsmith.moviefinder.core.repositories.MovieRepository
import me.zwsmith.moviefinder.core.services.PopularMoviesResponse
import javax.inject.Inject

class GetPopularMoviesInteractor @Inject constructor(private val movieRepository: MovieRepository) {
    val popularMoviesStream: Observable<ResponseStatus<PopularMoviesResponse>> =
            movieRepository.popularMoviesStream
}

class RefreshPopularMoviesInteractor @Inject constructor(
        private val movieRepository: MovieRepository
) {
    fun refreshPopularMovies() = movieRepository.refreshPopularMovies()
}