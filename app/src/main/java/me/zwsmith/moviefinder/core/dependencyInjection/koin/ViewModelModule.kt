package me.zwsmith.moviefinder.core.dependencyInjection.koin

import me.zwsmith.moviefinder.presentation.movieBrowser.MovieBrowserViewModel
import me.zwsmith.moviefinder.presentation.movieDetails.MovieDetailsViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { MovieDetailsViewModel(get()) }
    viewModel { MovieBrowserViewModel(get(), get(), get()) }
}