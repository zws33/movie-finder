package me.zwsmith.moviefinder.core.models.extensions

import me.zwsmith.moviefinder.core.models.PopularMovie
import kotlin.math.roundToInt

data class MovieListItem(
    val id: String,
    val title: String,
    val popularity: Int,
    val posterPath: String?
)

fun PopularMovie.toMovieListItem(): MovieListItem {
    return MovieListItem(
        id.toString(),
        title,
        popularity.roundToInt(),
        posterPath
    )
}