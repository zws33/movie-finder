package me.zwsmith.moviefinder.core.dependencyInjection.koin

import me.zwsmith.moviefinder.core.interactors.GetMovieDetailsInteractor
import me.zwsmith.moviefinder.core.interactors.GetPopularMoviesInteractor
import me.zwsmith.moviefinder.core.interactors.RefreshPopularMoviesInteractor
import me.zwsmith.moviefinder.core.interactors.RequestNextPopularMoviesPageInteractor
import org.koin.dsl.module.module

val interactorModule = module {
    single { GetPopularMoviesInteractor(get()) }
    single { RefreshPopularMoviesInteractor(get()) }
    single { RequestNextPopularMoviesPageInteractor(get()) }
    single { GetMovieDetailsInteractor(get()) }
}