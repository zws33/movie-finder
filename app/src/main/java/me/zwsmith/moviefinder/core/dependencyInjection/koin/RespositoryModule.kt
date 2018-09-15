package me.zwsmith.moviefinder.core.dependencyInjection.koin

import me.zwsmith.moviefinder.core.repositories.MovieRepository
import me.zwsmith.moviefinder.core.repositories.MovieRepositoryImpl
import org.koin.dsl.module.module

val repositoryModule = module {
    single { MovieRepositoryImpl(get()) as MovieRepository }
}