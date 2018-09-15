package me.zwsmith.moviefinder.core.dependencyInjection.koin

import me.zwsmith.moviefinder.core.repositories.MovieRepository
import org.koin.dsl.module.module

val repositoryModule = module {
    single { MovieRepository(get()) }
}