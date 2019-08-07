package me.zwsmith.moviefinder.presentation.movieBrowser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_movie_browser.*
import me.zwsmith.moviefinder.R
import me.zwsmith.moviefinder.presentation.extensions.isVisible
import me.zwsmith.moviefinder.presentation.extensions.observe
import me.zwsmith.moviefinder.presentation.movieDetails.MovieDetailsFragment
import org.koin.android.viewmodel.ext.android.viewModel

class MovieBrowserFragment : Fragment() {

    private val viewModel: MovieBrowserViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_movie_browser, container, false)
    }

    private val movieBrowserAdapter = MovieBrowserAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(movies_rv) {
            layoutManager = LinearLayoutManager(context)
            adapter = movieBrowserAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        observe(viewModel.viewStates) {
            updateView(it)
        }
        observe(viewModel.movieSelection) {
            navigateToDetails(it)
        }
        viewModel.loadMovies()
    }

    private fun navigateToDetails(movieId: String) {
        fragmentManager?.commit {
            add(R.id.root, MovieDetailsFragment.newInstance(movieId))
            addToBackStack(null)
        }
    }


    private fun updateView(viewState: MovieBrowserViewState) {
        progress.isVisible = viewState.isLoadingVisible
        error_group.isVisible = viewState.isErrorVisible
        movies_rv.isVisible = viewState.moviesList != null
        viewState.moviesList?.let { movieBrowserAdapter.updateItems(it) }
    }

    companion object {
        private val TAG = MovieBrowserFragment::class.java.simpleName

        fun newInstance(): Fragment {
            return MovieBrowserFragment()
        }
    }
}