package me.zwsmith.moviefinder.core.dependencyInjection.dagger

import dagger.Component
import me.zwsmith.moviefinder.presentation.movieDetails.MovieDetailsFragment
import me.zwsmith.moviefinder.presentation.movieBrowser.MovieBrowserFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    ViewModelModule::class,
    ServiceModule::class,
    RepositoryModule::class
])
interface Injector {
    fun inject(browserFragment: MovieBrowserFragment)
    fun inject(fragment: MovieDetailsFragment)
}