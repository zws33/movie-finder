package me.zwsmith.moviefinder.core.models.extensions

import me.zwsmith.moviefinder.core.models.MovieDetailsResponse

data class MovieDetails(
    val backdropPath: String?,
    val title: String,
    val overview: String?
)

fun MovieDetailsResponse.toMoveDetails(): MovieDetails {
    return MovieDetails(
        backdropPath,
        title,
        overview
    )
}