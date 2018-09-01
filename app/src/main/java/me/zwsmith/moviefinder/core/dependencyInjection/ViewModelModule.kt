package me.zwsmith.moviefinder.core.dependencyInjection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import me.zwsmith.moviefinder.presentation.movieResults.MovieResultsViewModel

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MovieResultsViewModel::class)
    internal abstract fun movieResultsViewModel(viewModel: MovieResultsViewModel): ViewModel

}