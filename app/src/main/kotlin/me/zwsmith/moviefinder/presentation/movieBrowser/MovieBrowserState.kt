package me.zwsmith.moviefinder.presentation.movieBrowser

sealed class MovieBrowserState {
    object Loading : MovieBrowserState() {
        override fun toString(): String = Loading::class.java.simpleName
    }

    object Error : MovieBrowserState() {
        override fun toString(): String = Loading::class.java.simpleName
    }

    data class Success(
            val movies: List<MovieBrowserItem>
    ) : MovieBrowserState()
}

data class MovieBrowserItem(
        val id: String,
        val title: String,
        val popularity: Double,
        val posterPath: String?
)