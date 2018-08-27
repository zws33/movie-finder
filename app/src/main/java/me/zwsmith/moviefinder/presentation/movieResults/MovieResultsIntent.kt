package me.zwsmith.moviefinder.presentation.movieResults

sealed class MovieResultsIntent {
    object RefreshPopularMovies : MovieResultsIntent() {
        override fun toString(): String = RefreshPopularMovies::class.java.simpleName
    }

    object LoadNextPopularMoviesPage : MovieResultsIntent() {
        override fun toString(): String = LoadNextPopularMoviesPage::class.java.simpleName
    }
}