package me.zwsmith.moviefinder.core.repositories

import me.zwsmith.moviefinder.core.models.extensions.MovieDetails
import me.zwsmith.moviefinder.core.models.extensions.MovieListItem
import me.zwsmith.moviefinder.core.models.extensions.toMoveDetails
import me.zwsmith.moviefinder.core.models.extensions.toMovieListItem
import me.zwsmith.moviefinder.core.services.Genre
import me.zwsmith.moviefinder.core.services.MovieService

class MovieRepositoryImpl(private val movieService: MovieService) : MovieRepository {
    override suspend fun getPopularMovies(page: Int): List<MovieListItem> {

        return movieService.getPopularMovies(page).popularMovies.map { it.toMovieListItem() }
    }

    override suspend fun getMovieDetailsById(id: String): MovieDetails {

        return movieService.getMovieDetailsById(id).toMoveDetails()
    }

    override suspend fun getGenres(): List<Genre> {
        return movieService.getGenres().genres
    }

    companion object {
        private val TAG = MovieRepositoryImpl::class.java.simpleName
    }
}

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): List<MovieListItem>
    suspend fun getMovieDetailsById(id: String): MovieDetails
    suspend fun getGenres(): List<Genre>
}