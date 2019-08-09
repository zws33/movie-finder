package me.zwsmith.moviefinder.core.models.extensions

import me.zwsmith.moviefinder.core.models.PopularMovie
import kotlin.math.roundToInt

data class MovieListItem(
    val id: String,
    val title: String,
    val rating: Int,
    val genreIds: List<Int>,
    val posterPath: String?
)

fun PopularMovie.toMovieListItem(): MovieListItem {
    return MovieListItem(
        id.toString(),
        title,
        voteAverage.roundToInt(),
        genreIds,
        posterPath
    )
}