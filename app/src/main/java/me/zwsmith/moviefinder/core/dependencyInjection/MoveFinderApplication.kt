package me.zwsmith.moviefinder.core.dependencyInjection

import android.app.Application
import dagger.Component
import me.zwsmith.moviefinder.presentation.movieResults.MovieResultsFragment
import javax.inject.Singleton

class MoveFinderApplication : Application() {

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

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class, ServiceModule::class])
interface Injector {
    fun inject(fragment: MovieResultsFragment)
}