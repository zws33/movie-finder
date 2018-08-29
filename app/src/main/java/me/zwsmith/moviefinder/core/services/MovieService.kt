package me.zwsmith.moviefinder.core.services

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("discover/movie?language=en-US&sort_by=popularity.desc")
    fun getPopularMovies(@Query("page") pageNumber: Int): Single<PopularMoviesResponse>

    @GET("movie/{id}")
    fun getMovieDetailsById(@Path("id") id: String): Single<MovieDetailsResponse>

    companion object {
        private val TAG = MovieService::class.java.simpleName
    }
}

data class PopularMoviesResponse(
        val page: Int,
        @SerializedName("results")
        val popularMovies: List<PopularMovie>,
        @SerializedName("total_results")
        val totalResults: Int,
        @SerializedName("total_pages")
        val totalPages: Int
)

data class PopularMovie(
        @SerializedName("poster_path")
        val posterPath: String?,
        val adult: Boolean,
        val overview: String,
        @SerializedName("release_date")
        val releaseDate: String,
        @SerializedName("genre_ids")
        val genreIds: List<Int>,
        val id: Int,
        @SerializedName("original_title")
        val originalTitle: String,
        @SerializedName("original_language")
        val originalLanguage: String,
        val title: String,
        @SerializedName("backdrop_path")
        val backdropPath: String,
        val popularity: Double,
        @SerializedName("vote_count")
        val voteCount: Int,
        val video: Boolean,
        @SerializedName("vote_average")
        val voteAverage: Double
)

data class MovieDetailsResponse(
        val adult: Boolean,
        @SerializedName("backdrop_path")
        val backdropPath: String?,
        @SerializedName("belongs_to_collection")
        val belongsToCollection: Map<String, Any>?,
        val budget: Int,
        val genres: List<Map<String, Any>>,
        val homepage: String?,
        val id: Int,
        @SerializedName("imdb_id")
        val imdbId: String?,
        @SerializedName("original_language")
        val originalLanguage: String,
        @SerializedName("original_title")
        val originalTitle: String,
        val overview: String?,
        val popularity: Double,
        @SerializedName("poster_path")
        val posterPath: String?,
        @SerializedName("production_companies")
        val productionCompanies: List<ProductionCompany>,
        @SerializedName("production_countries")
        val productionCountries: List<ProductionCountry>,
        @SerializedName("release_date")
        val releaseDate: String,
        val revenue: Int,
        val runtime: Int?,
        @SerializedName("spoken_languages")
        val spokenLanguages: List<SpokenLanguage>,
        val status: String,
        val tagline: String?,
        val title: String,
        val video: Boolean,
        @SerializedName("vote_average")
        val voteAverage: Double,
        @SerializedName("vote_count")
        val voteCount: Int
)

data class ProductionCompany(
        val name: String,
        val id: Int,
        @SerializedName("logo_path")
        val logoPath: String?,
        @SerializedName("origin_country")
        val origin_country: String
)

data class ProductionCountry(
        @SerializedName("iso_3166_1")
        val iso: String,
        val name: String
)

data class SpokenLanguage(
        @SerializedName("iso_639_1")
        val iso: String,
        val name: String
)