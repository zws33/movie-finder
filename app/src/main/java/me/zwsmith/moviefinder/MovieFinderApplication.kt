package me.zwsmith.moviefinder

import android.app.Application
import me.zwsmith.moviefinder.core.dependencyInjection.koin.*
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
            interactorModule,
            viewModelModule
    )
}