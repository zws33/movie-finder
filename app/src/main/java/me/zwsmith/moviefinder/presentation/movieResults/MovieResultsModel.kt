package me.zwsmith.moviefinder.presentation.movieResults

sealed class MovieResultsState {
    object Loading : MovieResultsState() {
        override fun toString(): String = Loading::class.java.simpleName
    }

    object Error : MovieResultsState() {
        override fun toString(): String = Loading::class.java.simpleName
    }

    data class Success(
            val movieResults: List<MovieResultItem>
    ) : MovieResultsState()
}

data class MovieResultItem(
        val id: String,
        val title: String,
        val popularity: Double,
        val posterPath: String?
)