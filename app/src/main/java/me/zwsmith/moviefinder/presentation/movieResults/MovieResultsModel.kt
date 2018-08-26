package me.zwsmith.moviefinder.presentation.movieResults

sealed class MovieResultsState {
    object Loading : MovieResultsState()
    object Error : MovieResultsState()
    data class Success(
            val movieResults: List<Movie>
    ) : MovieResultsState()
}

data class Movie(
        val id: String,
        val title: String,
        val genres: List<Int>,
        val posterPath: String
)