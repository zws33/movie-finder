package me.zwsmith.moviefinder.core.services

import me.zwsmith.moviefinder.core.models.MovieDetailsResponse
import me.zwsmith.moviefinder.core.models.MovieResultsResponse

interface MovieService {
    fun getPopularMovies(pageNumber: Int): MovieResultsResponse

    fun getMovieDetailsById(id: String): MovieDetailsResponse

    companion object {
        private val TAG = MovieService::class.java.simpleName
    }
}