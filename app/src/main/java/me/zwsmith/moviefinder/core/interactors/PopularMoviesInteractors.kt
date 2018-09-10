package me.zwsmith.moviefinder.core.interactors

import io.reactivex.Observable
import io.reactivex.Single
import me.zwsmith.moviefinder.core.common.ResponseStatus
import me.zwsmith.moviefinder.core.repositories.MovieRepository
import me.zwsmith.moviefinder.core.services.MovieDetailsResponse
import me.zwsmith.moviefinder.core.services.PopularMoviesResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetPopularMoviesInteractor @Inject constructor(
        private val movieRepository: MovieRepository
) {
    val popularMoviesStream: Observable<ResponseStatus<PopularMoviesResponse>> =
            movieRepository.popularMoviesStream
}

@Singleton
class RefreshPopularMoviesInteractor @Inject constructor(
        private val movieRepository: MovieRepository
) {
    fun refreshPopularMovies() = movieRepository.refreshPopularMovies()
}

@Singleton
class RequestNextPopularMoviesPageInteractor @Inject constructor(
        private val movieRepository: MovieRepository
) {
    fun requestNextPage() {
        movieRepository.loadNextPopularMoviesPage()
    }
}

@Singleton
class GetMovieDetailsInteractor @Inject constructor(private val movieRepository: MovieRepository) {
    fun getMovieDetailsById(movieId: String): Single<ResponseStatus<MovieDetailsResponse>> {
        return movieRepository.getMovieDetailsById(movieId)
    }
}