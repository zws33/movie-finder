package me.zwsmith.moviefinder.core.dependencyInjection.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import me.zwsmith.moviefinder.presentation.movieDetails.MovieDetailsViewModel
import me.zwsmith.moviefinder.presentation.movieBrowser.MovieResultsViewModel

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MovieResultsViewModel::class)
    internal abstract fun movieResultsViewModel(viewModel: MovieResultsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    internal abstract fun movieDetailsViewModel(viewModel: MovieDetailsViewModel): ViewModel
}