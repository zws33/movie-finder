package me.zwsmith.moviefinder.core.interactors

import io.reactivex.Single
import me.zwsmith.moviefinder.core.repositories.MovieRepository
import me.zwsmith.moviefinder.core.services.PopularMoviesResponse
import javax.inject.Inject

class GetPopularMoviesInteractor @Inject constructor(private val movieRepository: MovieRepository) {
    fun getPopularMovies(): Single<PopularMoviesResponse> {
        return movieRepository.getPopularMovies()
    }
}