package me.zwsmith.moviefinder.core.dependencyInjection.koin

import me.zwsmith.moviefinder.core.repositories.MovieRepository
import me.zwsmith.moviefinder.core.repositories.MovieRepositoryImpl
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val repositoryModule: Module = module {
    single<MovieRepository> { MovieRepositoryImpl(movieService = get()) }
}