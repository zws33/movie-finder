package me.zwsmith.moviefinder.core.dependencyInjection

import dagger.Component
import me.zwsmith.moviefinder.core.repositories.MovieRepository
import javax.inject.Singleton

@Singleton
@Component(
        modules = [AppModule::class, ServiceModule::class]
)
interface AppComponent {
    val application: MoveFinderApplication
    val movieRepository: MovieRepository

    fun inject(application: MoveFinderApplication)
}
