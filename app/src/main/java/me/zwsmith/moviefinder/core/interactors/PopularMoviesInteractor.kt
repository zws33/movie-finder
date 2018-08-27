package me.zwsmith.moviefinder.core.interactors

import io.reactivex.Observable
import me.zwsmith.moviefinder.core.repositories.MovieRepository
import me.zwsmith.moviefinder.core.services.PopularMoviesResponse
import javax.inject.Inject

class GetPopularMoviesInteractor @Inject constructor(private val movieRepository: MovieRepository) {
    val popularMoviesStream: Observable<PopularMoviesResponse> = movieRepository.popularMoviesStream
}

class RefreshPopularMoviesInteractor @Inject constructor(
        private val movieRepository: MovieRepository
) {
    fun refreshPopularMovies() = movieRepository.refreshPopularMovies()
}