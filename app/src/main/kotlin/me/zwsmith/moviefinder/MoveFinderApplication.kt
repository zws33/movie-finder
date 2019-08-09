package me.zwsmith.moviefinder

import android.app.Application
import me.zwsmith.moviefinder.core.dependencyInjection.dagger.Injector

class MovieFinderApplication : Application() {

    lateinit var injector: Injector private set
}