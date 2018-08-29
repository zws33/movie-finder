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
import me.zwsmith.moviefinder.core.interactors.GetPopularMoviesStreamInteractor
import me.zwsmith.moviefinder.core.interactors.RefreshPopularMoviesInteractor
import me.zwsmith.moviefinder.core.interactors.RequestNextPopularMoviesPageInteractor
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass


class MovieResultsViewModel @Inject constructor(
        private val refreshPopularMoviesInteractor: RefreshPopularMoviesInteractor,
        getPopularMoviesStreamInteractor: GetPopularMoviesStreamInteractor,
        private val requestNextPopularMoviesPageInteractor: RequestNextPopularMoviesPageInteractor
) : ViewModel() {

    val intentRelay = PublishRelay.create<MovieResultsIntent>()

    private val stateStream = buildMovieResultsStateStream(
            intentStream = intentRelay,
            refreshPopularMovies =
            { refreshPopularMoviesInteractor.refreshPopularMovies() }.toCompletable(),
            loadNextPopularMoviesPage =
            { requestNextPopularMoviesPageInteractor.requestNextPage() }.toCompletable(),
            movieResultsStream = getPopularMoviesStreamInteractor.popularMoviesStream
    )

    fun getMovieListViewStateStream(): Observable<MovieResultsViewState> {
        return stateStream.map { state ->
            when (state) {
                is MovieResultsState.Success -> {
                    MovieResultsViewState(
                            isLoadingVisible = false,
                            isErrorVisible = false,
                            retryIntent = null,
                            movieResults = state.movieResults.map { movie ->
                                movie.toResultItemViewState()
                            }
                    )
                }
                MovieResultsState.Loading -> {
                    MovieResultsViewState(
                            isLoadingVisible = true,
                            isErrorVisible = false,
                            retryIntent = null,
                            movieResults = null
                    )
                }
                MovieResultsState.Error -> {
                    MovieResultsViewState(
                            isLoadingVisible = true,
                            isErrorVisible = true,
                            retryIntent = MovieResultsIntent.RefreshPopularMovies,
                            movieResults = null
                    )
                }
                is MovieResultsState.NavigateToMovieDetails -> TODO()
            }
        }
    }

    fun loadNextPage() {
        intentRelay.accept(MovieResultsIntent.LoadNextPopularMoviesPage)
    }

    private fun Movie.toResultItemViewState(): MovieResultsItemViewState {
        return MovieResultsItemViewState(
                id,
                title,
                genres.joinToString(", "),
                posterPath?.let { IMAGE_BASE_URL + it }
        )
    }

    companion object {
        private val TAG = MovieResultsViewModel::class.java.simpleName
        private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w45"
    }
}

data class MovieResultsViewState(
        val isLoadingVisible: Boolean,
        val isErrorVisible: Boolean,
        val retryIntent: MovieResultsIntent.RefreshPopularMovies?,
        val movieResults: List<MovieResultsItemViewState>?
)

data class MovieResultsItemViewState(
        val id: String,
        val title: String,
        val genres: String,
        val imageUrl: String?
)


@Suppress("UNCHECKED_CAST")
@Singleton
class ViewModelFactory @Inject constructor(
        private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T
}

@Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER
)
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