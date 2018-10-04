package me.zwsmith.moviefinder.core.dependencyInjection.dagger

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import me.zwsmith.moviefinder.MovieFinderApplication
import me.zwsmith.moviefinder.core.common.ApiKeyInterceptor
import me.zwsmith.moviefinder.core.common.Logger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class AppModule(private val application: MovieFinderApplication) {
    @Provides
    @Singleton
    fun provideContext(): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
    }

    @Provides
    @Singleton
    fun provideLogger(logger: Logger): Logger = logger

    @Provides
    @Singleton
    fun provideRetroFit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .build()
    }

    @Provides
    @Singleton
    fun provideApiKeyInterceptor(): ApiKeyInterceptor {
        return ApiKeyInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(apiKeyInterceptor: ApiKeyInterceptor): OkHttpClient {
        val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

        return OkHttpClient.Builder()
                .addInterceptor(apiKeyInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()
    }

    companion object {
        private val TAG = AppModule::class.java.simpleName
        private const val BASE_URL = "https://api.themoviedb.org/3/"
    }
}