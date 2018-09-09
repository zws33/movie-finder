package me.zwsmith.moviefinder.core.dependencyInjection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import me.zwsmith.moviefinder.presentation.movieDetails.MovieDetailsViewModel
import me.zwsmith.moviefinder.presentation.movieResults.MovieBrowserViewModel

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MovieBrowserViewModel::class)
    internal abstract fun movieResultsViewModel(viewModel: MovieBrowserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    internal abstract fun movieDetailsViewModel(viewModel: MovieDetailsViewModel): ViewModel
}