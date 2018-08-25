package me.zwsmith.moviefinder.presentation.movieResults

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.zwsmith.moviefinder.R

/**
 * Created by RBI Engineers on 8/25/18.
 */
class MovieResultsFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_movie_results, container, false)
    }

    companion object {
        private val TAG = MovieResultsFragment::class.java.simpleName

        fun newInstance(): Fragment {
            return MovieResultsFragment()
        }
    }
}