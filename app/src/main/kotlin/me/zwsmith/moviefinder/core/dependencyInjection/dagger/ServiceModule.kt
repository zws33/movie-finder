package me.zwsmith.moviefinder.core.dependencyInjection.dagger

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import me.zwsmith.moviefinder.core.services.MovieSericeImpl
import me.zwsmith.moviefinder.core.services.RxMovieService
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class ServiceModule {
    @Provides
    @Singleton
    fun provideMovieService(okHttpClient: OkHttpClient, gson: Gson): RxMovieService {
        return MovieSericeImpl(okHttpClient, gson)
    }
}