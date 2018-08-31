package me.zwsmith.moviefinder

import com.jakewharton.rxrelay2.PublishRelay
import io.kotlintest.specs.FreeSpec
import io.reactivex.Observable
import io.reactivex.rxkotlin.toCompletable
import me.zwsmith.moviefinder.core.common.toSuccessComplete
import me.zwsmith.moviefinder.core.extensions.just
import me.zwsmith.moviefinder.core.services.PopularMovie
import me.zwsmith.moviefinder.core.services.PopularMoviesResponse
import me.zwsmith.moviefinder.presentation.movieResults.Movie
import me.zwsmith.moviefinder.presentation.movieResults.MovieResultsIntent
import me.zwsmith.moviefinder.presentation.movieResults.MovieResultsState
import me.zwsmith.moviefinder.presentation.movieResults.buildMovieResultsStateStream
import org.amshove.kluent.shouldEqual

class MovieResultsModelTest : FreeSpec({
    "When build state stream is called, it should return a stream whose first emission is loading" {
        val popularMovies = listOf<PopularMovie>()
        val intentStream: Observable<MovieResultsIntent> = PublishRelay.create()
        val stateStream = buildMovieResultsStateStream(
                intentStream = intentStream,
                loadNextPopularMoviesPage = {}.toCompletable(),
                refreshPopularMovies = {}.toCompletable(),
                movieResultsStream = PopularMoviesResponse(
                        1, popularMovies, 10, 1
                ).toSuccessComplete().just()
        )
        stateStream.test().events.first().first().shouldEqual(MovieResultsState.Loading)
    }
    "When build state stream is called," - {

        val popularMovies = listOf<PopularMovie>()
        val movies = listOf<Movie>()
        val intentStream: Observable<MovieResultsIntent> = PublishRelay.create()
        val stateStream = buildMovieResultsStateStream(
                intentStream = intentStream,
                loadNextPopularMoviesPage = {}.toCompletable(),
                refreshPopularMovies = {}.toCompletable(),
                movieResultsStream = PopularMoviesResponse(
                        1, popularMovies, 10, 1
                ).toSuccessComplete().just()
        )

        "it should return a stream whose first emission is loading and then success" {
            stateStream.test().events.first().shouldEqual(
                    listOf(MovieResultsState.Loading, MovieResultsState.Success(movies))
            )
        }
    }
    "When build state stream is called, and request more data intent is fired" - {
        val popularMovies = listOf<PopularMovie>()
        val movies = listOf<Movie>()
        val intentStream = PublishRelay.create<MovieResultsIntent>()
        val stateStream = buildMovieResultsStateStream(
                intentStream = intentStream,
                loadNextPopularMoviesPage = {}.toCompletable(),
                refreshPopularMovies = {}.toCompletable(),
                movieResultsStream = PopularMoviesResponse(
                        1, popularMovies, 10, 1
                ).toSuccessComplete().just()
        )
        "state stream should emit load, success, success" {
            intentStream.accept(MovieResultsIntent.LoadNextPopularMoviesPage)
            stateStream.test().events.first().shouldEqual(
                    listOf(
                            MovieResultsState.Loading,
                            MovieResultsState.Success(movies),
                            MovieResultsState.Success(movies))
            )
        }
    }

})