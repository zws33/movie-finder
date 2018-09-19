package me.zwsmith.moviefinder.core.dependencyInjection.dagger

import dagger.Module
import dagger.Provides
import me.zwsmith.moviefinder.core.services.MovieService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ServiceModule {
    @Provides
    @Singleton
    fun provideMovieService(retrofit: Retrofit): MovieService {
        return retrofit.create(MovieService::class.java)
    }
}