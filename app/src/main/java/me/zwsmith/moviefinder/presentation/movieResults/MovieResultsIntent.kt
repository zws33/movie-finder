package me.zwsmith.moviefinder.presentation.movieResults

sealed class MovieResultsIntent {
    data class NavigateToMovieDetails(val id: String) : MovieResultsIntent()
    object RefreshPopularMovies : MovieResultsIntent() {
        override fun toString(): String = RefreshPopularMovies::class.java.simpleName
    }
}