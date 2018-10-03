package me.zwsmith.moviefinder.core.dependencyInjection.dagger

import dagger.Module
import dagger.Provides
import me.zwsmith.moviefinder.core.services.RxMovieService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ServiceModule {
    @Provides
    @Singleton
    fun provideRxMovieService(retrofit: Retrofit): RxMovieService {
        return retrofit.create(RxMovieService::class.java)
    }
}