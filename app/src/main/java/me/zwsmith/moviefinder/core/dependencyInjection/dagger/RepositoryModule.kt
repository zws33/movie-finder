package me.zwsmith.moviefinder.core.dependencyInjection.dagger

import dagger.Binds
import dagger.Module
import me.zwsmith.moviefinder.core.repositories.MovieRepository
import me.zwsmith.moviefinder.core.repositories.MovieRepositoryImpl

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun provideMovieRepository(movieRepository: MovieRepositoryImpl): MovieRepository
}