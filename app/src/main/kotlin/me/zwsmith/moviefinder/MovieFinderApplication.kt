package me.zwsmith.moviefinder

import android.app.Application
import me.zwsmith.moviefinder.core.dependencyInjection.koin.appModule
import me.zwsmith.moviefinder.core.dependencyInjection.koin.repositoryModule
import me.zwsmith.moviefinder.core.dependencyInjection.koin.serviceModule
import me.zwsmith.moviefinder.core.dependencyInjection.koin.viewModelModule
import org.koin.android.ext.android.startKoin

class MovieFinderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, koinModules)
    }

    private val koinModules = listOf(
            appModule,
            serviceModule,
            repositoryModule,
            viewModelModule
    )
}