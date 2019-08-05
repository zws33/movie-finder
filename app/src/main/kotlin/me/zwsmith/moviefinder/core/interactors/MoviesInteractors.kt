package me.zwsmith.moviefinder.core.interactors

import io.reactivex.Observable
import io.reactivex.Single
import me.zwsmith.moviefinder.core.common.ResponseStatus
import me.zwsmith.moviefinder.core.repositories.MovieRepository
import me.zwsmith.moviefinder.core.services.MovieDetailsResponse
import me.zwsmith.moviefinder.core.services.PopularMoviesResponse

class GetPopularMoviesInteractor(private val movieRepository: MovieRepository) {
    val popularMoviesStream: Observable<ResponseStatus<PopularMoviesResponse>> =
            movieRepository.popularMoviesStream
}

class RefreshPopularMoviesInteractor(private val movieRepository: MovieRepository) {
    fun refreshPopularMovies() = movieRepository.refreshPopularMovies()
}

class RequestNextPopularMoviesPageInteractor(private val movieRepository: MovieRepository) {
    fun requestNextPage() {
        movieRepository.loadNextPopularMoviesPage()
    }
}

class GetMovieDetailsInteractor(private val movieRepository: MovieRepository) {
    fun getMovieDetailsById(movieId: String): Single<ResponseStatus<MovieDetailsResponse>> {
        return movieRepository.getMovieDetailsById(movieId)
    }
}