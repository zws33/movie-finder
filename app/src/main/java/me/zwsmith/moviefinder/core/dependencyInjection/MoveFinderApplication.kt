package me.zwsmith.moviefinder.core.dependencyInjection

import android.app.Application

class MoveFinderApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }
}