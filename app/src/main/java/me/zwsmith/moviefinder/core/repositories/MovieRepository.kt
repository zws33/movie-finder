package me.zwsmith.moviefinder.core.repositories

import io.reactivex.Single
import me.zwsmith.moviefinder.core.services.MovieDetailsResponse
import me.zwsmith.moviefinder.core.services.MovieService
import me.zwsmith.moviefinder.core.services.PopularMoviesResponse
import javax.inject.Inject

class MovieRepository @Inject constructor(private val movieService: MovieService) {
    fun getPopularMovies(): Single<PopularMoviesResponse> {
        return movieService.getPopularMovies()
    }

    fun getMovieDetailsById(id: String): Single<MovieDetailsResponse> {
        return movieService.getMovieDetailsById(id)
    }
}