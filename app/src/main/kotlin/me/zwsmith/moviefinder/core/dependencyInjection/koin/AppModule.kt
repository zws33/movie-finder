package me.zwsmith.moviefinder.core.dependencyInjection.koin

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import me.zwsmith.moviefinder.BuildConfig
import me.zwsmith.moviefinder.core.common.ApiKeyInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { provideGson() }
    single { provideApiKeyInterceptor() }
    single { provideOkHttpClient(apiKeyInterceptor = get()) }
    single { provideRetroFitInstance(gson = get(), okHttpClient = get()) }
    single { providePicasso(appContext = get()) }
}

private fun provideRetroFitInstance(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

private fun provideApiKeyInterceptor(): ApiKeyInterceptor {
    return ApiKeyInterceptor()
}

private fun provideOkHttpClient(apiKeyInterceptor: ApiKeyInterceptor): OkHttpClient {
    val logginInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }
    return OkHttpClient.Builder()
        .addInterceptor(apiKeyInterceptor)
        .addInterceptor(logginInterceptor)
        .build()
}

private fun provideGson(): Gson = GsonBuilder()
    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    .create()

private fun providePicasso(appContext: Context): Picasso =
    Picasso.Builder(appContext).loggingEnabled(true).build()

private const val BASE_URL = "https://api.themoviedb.org/3/"
