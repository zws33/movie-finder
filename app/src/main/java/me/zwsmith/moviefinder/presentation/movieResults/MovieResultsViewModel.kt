package me.zwsmith.moviefinder.presentation.movieResults

import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import me.zwsmith.moviefinder.core.extensions.emitAtInterval
import me.zwsmith.moviefinder.core.repositories.MovieRepository
import javax.inject.Inject

class MovieResultsViewModel @Inject constructor(
        private val movieRepository: MovieRepository
) : ViewModel() {

    fun getPopularMovies() {
        movieRepository.getPopularMovies().subscribeBy(
                onSuccess = { response ->
                    Log.d(TAG, response.toString())
                },
                onError = { e ->
                    Log.e(TAG, "Error message: ${e.message}", e)
                }
        )
    }

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
                    MovieResultIntent.GetMovieDetails("1")
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
        val intent: MovieResultIntent
)

sealed class MovieResultIntent {
    data class GetMovieDetails(val id: String) : MovieResultIntent()
}
