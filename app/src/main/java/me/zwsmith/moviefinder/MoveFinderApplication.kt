package me.zwsmith.moviefinder

import android.app.Application
import me.zwsmith.moviefinder.core.dependencyInjection.dagger.AppModule
import me.zwsmith.moviefinder.core.dependencyInjection.dagger.DaggerInjector
import me.zwsmith.moviefinder.core.dependencyInjection.dagger.Injector
import me.zwsmith.moviefinder.core.dependencyInjection.koin.*
import org.koin.android.ext.android.startKoin

class MoveFinderApplication : Application() {

    lateinit var injector: Injector private set

    override fun onCreate() {
        super.onCreate()
        initDagger()
        startKoin(this, koinModules)
    }

    private val koinModules = listOf(
            appModule,
            serviceModule,
            repositoryModule,
            interactorModule,
            viewModelModule
    )

    private fun initDagger() {
        injector = DaggerInjector.builder()
                .appModule(AppModule(this))
                .build()
    }
}