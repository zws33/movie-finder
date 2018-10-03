package me.zwsmith.moviefinder.core.services

import me.zwsmith.moviefinder.core.models.MovieDetailsResponse
import me.zwsmith.moviefinder.core.models.MovieListResponse

interface MovieService {
    fun getPopularMovies(pageNumber: Int): MovieListResponse

    fun getMovieDetailsById(id: String): MovieDetailsResponse

    companion object {
        private val TAG = MovieService::class.java.simpleName
    }
}