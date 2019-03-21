package me.zwsmith.moviefinder.core.dependencyInjection.kodein

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.zwsmith.moviefinder.core.common.ApiKeyInterceptor
import me.zwsmith.moviefinder.core.repositories.MovieRepository
import me.zwsmith.moviefinder.core.repositories.MovieRepositoryImpl
import me.zwsmith.moviefinder.core.services.MovieService
import me.zwsmith.moviefinder.presentation.movieBrowser.MovieBrowserViewModel
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val appModule = Kodein.Module(name = "AppModule") {
    bind<Gson>() with singleton { provideGson() }
    bind<ApiKeyInterceptor>() with singleton { provideApiKeyInterceptor() }
    bind<OkHttpClient>() with singleton { provideOkHttpClient(apiKeyInterceptor = instance()) }
    bind<Retrofit>() with singleton {
        provideRetroFitInstance(gson = instance(), okHttpClient = instance())
    }
}

val serviceModule = Kodein.Module(name = "ServiceModule") {
    bind<MovieService>() with singleton { createService<MovieService>(instance()) }
}

val repositoryModule = Kodein.Module(name = "RepositoryModule") {
    bind<MovieRepository>() with singleton {
        MovieRepositoryImpl(movieService = instance()) as MovieRepository
    }
}

val viewModelModule = Kodein.Module(name = "ViewModelModule") {
    bind<MovieBrowserViewModel>() with singleton {
        MovieBrowserViewModel(movieRepository = instance())
    }
    bind<MovieBrowserViewModelFactory>() with singleton {
        MovieBrowserViewModelFactory(movieBrowserViewModel = instance())
    }
}

@Suppress("UNCHECKED_CAST")
class MovieBrowserViewModelFactory constructor(
        private val movieBrowserViewModel: MovieBrowserViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieBrowserViewModel::class.java)) {
            return movieBrowserViewModel as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}


private inline fun <reified T> createService(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}

private fun provideGson(): Gson {
    return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
}

private fun provideApiKeyInterceptor(): ApiKeyInterceptor {
    return ApiKeyInterceptor()
}

private fun provideOkHttpClient(apiKeyInterceptor: ApiKeyInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .build()
}

private fun provideRetroFitInstance(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .build()
}

private const val BASE_URL = "https://api.themoviedb.org/3/"
