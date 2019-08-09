package me.zwsmith.moviefinder

import android.app.Application
import me.zwsmith.moviefinder.core.dependencyInjection.koin.appModule
import me.zwsmith.moviefinder.core.dependencyInjection.koin.repositoryModule
import me.zwsmith.moviefinder.core.dependencyInjection.koin.serviceModule
import me.zwsmith.moviefinder.core.dependencyInjection.koin.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MovieFinderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Koin Android logger
            androidLogger()
            //inject Android context
            androidContext(this@MovieFinderApplication)
            modules(listOf(
                appModule,
                serviceModule,
                repositoryModule,
                viewModelModule
            ))
        }
    }

}