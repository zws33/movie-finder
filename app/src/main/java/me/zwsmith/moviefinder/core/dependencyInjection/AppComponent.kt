package me.zwsmith.moviefinder.core.dependencyInjection

import com.squareup.picasso.Picasso
import dagger.Component
import me.zwsmith.moviefinder.core.repositories.MovieRepository
import javax.inject.Singleton

@Singleton
@Component(
        modules = [AppModule::class, ServiceModule::class]
)
interface AppComponent {
    val application: MoveFinderApplication
    val picasso: Picasso
    val movieRepository: MovieRepository

    fun inject(application: MoveFinderApplication)
}
