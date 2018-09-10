package me.zwsmith.moviefinder.core.dependencyInjection.koin

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.zwsmith.moviefinder.core.common.ApiKeyInterceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { provideGson() }
    single { provideApiKeyInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideRetroFitInstance(get(), get()) }
}


private fun provideRetroFitInstance(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .build()
}


private fun provideApiKeyInterceptor(): ApiKeyInterceptor {
    return ApiKeyInterceptor()
}


private fun provideOkHttpClient(apiKeyInterceptor: ApiKeyInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .build()
}

private fun provideGson(): Gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

private const val BASE_URL = "https://api.themoviedb.org/3/"
