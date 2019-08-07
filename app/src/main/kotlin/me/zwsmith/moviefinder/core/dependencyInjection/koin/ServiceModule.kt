package me.zwsmith.moviefinder.core.dependencyInjection.koin

import me.zwsmith.moviefinder.core.services.MovieService
import org.koin.dsl.module.module
import retrofit2.Retrofit

val serviceModule = module {
    single { createService<MovieService>(get()) }
}

private inline fun <reified T> createService(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}