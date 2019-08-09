package me.zwsmith.moviefinder.core.dependencyInjection.koin

import me.zwsmith.moviefinder.presentation.movieBrowser.MovieBrowserViewModel
import me.zwsmith.moviefinder.presentation.movieDetails.MovieDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MovieBrowserViewModel(movieRepository = get()) }
    viewModel { MovieDetailsViewModel(movieRepository = get()) }
}