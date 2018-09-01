package me.zwsmith.moviefinder.presentation.movieResults

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