package me.zwsmith.moviefinder.presentation.movieResults

import me.zwsmith.moviefinder.core.common.ResponseStatus
import me.zwsmith.moviefinder.core.services.PopularMoviesResponse

sealed class MovieResultsState {
    object Loading : MovieResultsState() {
        override fun toString(): String = Loading::class.java.simpleName
    }

    object Error : MovieResultsState() {
        override fun toString(): String = Loading::class.java.simpleName
    }

    data class Success(
            val movieResults: List<Movie>
    ) : MovieResultsState()
}

data class Movie(
        val id: String,
        val title: String,
        val genres: List<String>,
        val posterPath: String?
)

fun handleSuccess(
        responseStatus: ResponseStatus.Complete.Success<PopularMoviesResponse>
): MovieResultsState.Success {
    val response = responseStatus.value
    val movieList = response.popularMovies.map { popularMovie ->
        Movie(
                popularMovie.id.toString(),
                popularMovie.title,
                popularMovie.genreIds.map { it.toString() },
                popularMovie.posterPath
        )
    }
    return MovieResultsState.Success(movieList)
}