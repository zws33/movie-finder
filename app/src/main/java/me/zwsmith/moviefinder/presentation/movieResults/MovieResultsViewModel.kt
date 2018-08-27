package me.zwsmith.moviefinder.presentation.movieResults

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.jakewharton.rxrelay2.PublishRelay
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import io.reactivex.Observable
import io.reactivex.rxkotlin.toCompletable
import me.zwsmith.moviefinder.core.extensions.emitAtInterval
import me.zwsmith.moviefinder.core.interactors.GetPopularMoviesInteractor
import me.zwsmith.moviefinder.core.interactors.RefreshPopularMoviesInteractor
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass


class MovieResultsViewModel @Inject constructor(
        refreshPopularMoviesInteractor: RefreshPopularMoviesInteractor,
        getPopularMoviesInteractor: GetPopularMoviesInteractor
) : ViewModel() {

    val intentRelay = PublishRelay.create<MovieResultsIntent>()

    val stateStream = buildMovieResultsStateStream(
            intentStream = intentRelay,
            refreshPopularMovies =
            { refreshPopularMoviesInteractor.refreshPopularMovies() }.toCompletable(),
            movieResultsStream = getPopularMoviesInteractor.popularMoviesStream
    )


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


@Suppress("UNCHECKED_CAST")
@Singleton
class ViewModelFactory @Inject constructor(
        private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MovieResultsViewModel::class)
    internal abstract fun movieResultsViewModel(viewModel: MovieResultsViewModel): ViewModel

}