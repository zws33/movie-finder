package me.zwsmith.moviefinder.core

import android.app.Application
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by RBI Engineers on 8/25/18.
 */

@Singleton
@Component(modules = [AppModule::class])
interface CoreComponent {
    val application: MoveFinderApplication
}

@Module
class AppModule(private val application: MoveFinderApplication) {
    @Provides
    @Singleton
    fun provideApplication(): MoveFinderApplication {
        return application
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
    }

    @Provides
    @Singleton
    fun provideRetroFit(gson: Gson): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun provideApiKeyInterceptor(): Interceptor {

    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        OkHttpClient.Builder()
                .
    }
}

class MoveFinderApplication : Application()