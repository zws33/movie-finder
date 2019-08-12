package me.zwsmith.moviefinder.presentation.movieDetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_movie_details.*
import me.zwsmith.moviefinder.R
import me.zwsmith.moviefinder.presentation.extensions.observe
import org.koin.android.ext.android.inject

class MovieDetailsFragment : Fragment() {
    private val viewModel: MovieDetailsViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onStart() {
        super.onStart()
        observe(viewModel.viewStates) {
            updateView(it)
        }
        arguments?.getString(MOVIE_ID, null)?.let {
            viewModel.loadMovieDetails(it)
        } ?: Log.e(
            TAG,
            "Movie details fragment instantiated without movie id.",
            IllegalStateException("Movie details fragment instantiated without movie id.")
        )
    }

    private fun updateView(viewState: MovieDetailsViewState) {
        title.text = viewState.title
        overview.text = viewState.overview
        Picasso.get()
            .load(viewState.backdropUrl)
            .placeholder(R.drawable.ic_action_movie)
            .into(backdrop)
    }

    companion object {
        private val TAG = MovieDetailsFragment::class.java.simpleName
        fun newInstance(movieId: String) = MovieDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(MOVIE_ID, movieId)
            }
        }

        private const val MOVIE_ID = "movie_id"
    }
}