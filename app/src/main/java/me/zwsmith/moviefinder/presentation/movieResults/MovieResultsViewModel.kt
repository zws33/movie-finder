package me.zwsmith.moviefinder.presentation.movieResults

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import me.zwsmith.moviefinder.core.extensions.emitAtInterval
import javax.inject.Inject

class MovieResultsViewModel @Inject constructor() : ViewModel() {

    fun getMovieListViewStateStream(): Observable<MovieResultsViewState> {
        val success = MovieResultsViewState(
                isLoadingVisible = false,
                isErrorVisible = false,
                movieResults = getMovies(10)
        )
        val loading = MovieResultsViewState(
                isLoadingVisible = true,
                isErrorVisible = false,
                movieResults = null
        )
        val error = MovieResultsViewState(
                isLoadingVisible = false,
                isErrorVisible = true,
                movieResults = null
        )
        val viewStates = listOf(
                loading,
                error,
                loading,
                success,
                error
        )
        return emitAtInterval(viewStates, 2)
    }

    private fun getMovies(itemCount: Int): List<MovieResultsItemViewState> {
        val list = arrayListOf<MovieResultsItemViewState>()
        for (i in 1..itemCount) {
            val movieListItemViewState = MovieResultsItemViewState(
                    "Movie Title $i",
                    "Action, Adventure",
                    imageUrl,
                    MovieResultsIntent.NavigateToMovieDetails("1")
            )
            list.add(movieListItemViewState)
        }
        return list
    }

    companion object {
        private val TAG = MovieResultsViewModel::class.java.simpleName
        private const val imageUrl =
                "https://image.tmdb.org/t/p/w45/38bmEXmuJuInLs9dwfgOGCHmZ7l.jpg"
    }
}

data class MovieResultsViewState(
        val isLoadingVisible: Boolean,
        val isErrorVisible: Boolean,
        val movieResults: List<MovieResultsItemViewState>?
)

data class MovieResultsItemViewState(
        val title: String,
        val genres: String,
        val imageUrl: String,
        val intent: MovieResultsIntent
)