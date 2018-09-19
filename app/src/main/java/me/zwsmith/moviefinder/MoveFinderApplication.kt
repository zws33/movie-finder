package me.zwsmith.moviefinder

import android.app.Application
import me.zwsmith.moviefinder.core.dependencyInjection.dagger.AppModule
import me.zwsmith.moviefinder.core.dependencyInjection.dagger.DaggerInjector
import me.zwsmith.moviefinder.core.dependencyInjection.dagger.Injector

class MovieFinderApplication : Application() {

    lateinit var injector: Injector private set

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        injector = DaggerInjector.builder()
                .appModule(AppModule(this))
                .build()
    }
}

