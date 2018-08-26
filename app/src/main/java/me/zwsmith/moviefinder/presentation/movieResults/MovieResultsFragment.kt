package me.zwsmith.moviefinder.presentation.movieResults

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_movie_results.view.*
import me.zwsmith.moviefinder.R

/**
 * Created by RBI Engineers on 8/25/18.
 */
class MovieResultsFragment : Fragment() {

    private val movieListViewState = ArrayList<MovieListItemViewState>().apply {
        for (i in 1..50) {
            val listItem = MovieListItemViewState("Captain America $i", "Action, Adventure", imageUrl)
            add(listItem)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_movie_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view.movie_results_rv) {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            val movieListAdapter = MovieListAdapter(movieListViewState)
            adapter = movieListAdapter
        }
    }

    companion object {
        private val TAG = MovieResultsFragment::class.java.simpleName
        private const val imageUrl =
                "https://www.android.com/static/2016/img/share/andy-lg.png"

        fun newInstance(): Fragment {
            return MovieResultsFragment()
        }
    }
}