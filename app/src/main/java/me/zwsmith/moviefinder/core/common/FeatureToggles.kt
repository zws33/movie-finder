package me.zwsmith.moviefinder.core.common

import android.support.v4.app.Fragment
import me.zwsmith.moviefinder.presentation.movieBrowser.MovieBrowserFragment
import me.zwsmith.moviefinder.presentation.movieBrowser.MovieBrowserFragmentV2
import me.zwsmith.moviefinder.presentation.movieDetails.MovieDetailsFragment
import me.zwsmith.moviefinder.presentation.movieDetails.MovieDetailsFragmentV2

class FeatureToggles {
    companion object {

        const val KOIN_ENABLED = true

        fun getMovieBrowserFragement(): Fragment {
            return if (KOIN_ENABLED) {
                MovieBrowserFragmentV2.newInstance()
            } else {
                MovieBrowserFragment.newInstance()
            }
        }

        fun getMovieDetailsFragment(movieId: String): Fragment {
            return if (KOIN_ENABLED) {
                MovieDetailsFragmentV2.newInstance(movieId)
            } else {
                MovieDetailsFragment.newInstance(movieId)
            }
        }


        private val TAG = FeatureToggles::class.java.simpleName
    }
}