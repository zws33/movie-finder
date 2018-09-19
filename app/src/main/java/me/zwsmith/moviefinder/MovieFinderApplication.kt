package me.zwsmith.moviefinder

import android.app.Application
import me.zwsmith.moviefinder.core.dependencyInjection.kodein.appModule
import me.zwsmith.moviefinder.core.dependencyInjection.kodein.repositoryModule
import me.zwsmith.moviefinder.core.dependencyInjection.kodein.serviceModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class MovieFinderApplication : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        import(appModule)
        import(serviceModule)
        import(repositoryModule)
    }
}