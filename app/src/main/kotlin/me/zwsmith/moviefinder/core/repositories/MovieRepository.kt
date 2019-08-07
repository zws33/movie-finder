package me.zwsmith.moviefinder.core.repositories

import android.util.Log
import me.zwsmith.moviefinder.core.models.extensions.MovieDetails
import me.zwsmith.moviefinder.core.models.extensions.MovieListItem
import me.zwsmith.moviefinder.core.models.extensions.toMoveDetails
import me.zwsmith.moviefinder.core.models.extensions.toMovieListItem
import me.zwsmith.moviefinder.core.services.MovieService

class MovieRepositoryImpl(private val movieService: MovieService) : MovieRepository {
    override suspend fun getPopularMovies(page: Int): List<MovieListItem> {
        val response = try {
            movieService.getPopularMovies(page).also { Log.d(TAG, "PopularResponse = $it") }
        } catch (exception: Exception) {
            throw exception
        }
        val moviesList = response.body()?.popularMovies?.map { it.toMovieListItem() }
        return moviesList.also { Log.d(TAG, "MoviesList = ${it?.joinToString(",\n")}") }
            ?: throw IllegalStateException("Response.body() was null")
    }

    override suspend fun getMovieDetailsById(id: String): MovieDetails {
        val response = try {
            movieService.getMovieDetailsById(id)
        } catch (exception: Exception) {
            throw exception
        }
        return response.body()?.toMoveDetails()
            ?: throw IllegalStateException("Response.body() was null")
    }

    companion object {
        private val TAG = MovieRepositoryImpl::class.java.simpleName
    }
}

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): List<MovieListItem>
    suspend fun getMovieDetailsById(id: String): MovieDetails
}