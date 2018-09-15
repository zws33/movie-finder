package me.zwsmith.moviefinder.core.dependencyInjection.dagger

import dagger.Component
import me.zwsmith.moviefinder.presentation.movieBrowser.MovieBrowserFragment
import me.zwsmith.moviefinder.presentation.movieDetails.MovieDetailsFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class, ServiceModule::class])
interface Injector {
    fun inject(browserFragment: MovieBrowserFragment)
    fun inject(fragment: MovieDetailsFragment)
}