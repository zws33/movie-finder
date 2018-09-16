package me.zwsmith.moviefinder.presentation.movieDetails

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_movie_details.view.*
import me.zwsmith.moviefinder.R
import org.koin.android.viewmodel.ext.android.viewModel

class MovieDetailsFragmentV2 : Fragment() {
    private val movieDetailsViewModel: MovieDetailsViewModel by viewModel()
    private var compositeDisposable = CompositeDisposable()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onStart() {
        super.onStart()
        arguments?.getString(MOVIE_ID, null)?.let {
            compositeDisposable.add(
                    movieDetailsViewModel.getMovieDetailsViewState(it)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeBy(
                                    onSuccess = { movieDetailsViewState ->
                                        view?.update(movieDetailsViewState)
                                    },
                                    onError = { e -> Log.e(TAG, e.message, e) }
                            )
            )
        }
                ?: Log.e(
                        TAG,
                        "Movie details fragment instantiated without movie id.",
                        IllegalStateException("Movie details fragment instantiated without movie id.")
                )
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }

    private fun View.update(viewState: MovieDetailsViewState) {
        title.text = viewState.title
        overview.text = viewState.overview
        Picasso.get()
                .load(viewState.backdropUrl)
                .placeholder(R.drawable.ic_action_movie)
                .into(backdrop)
    }

    companion object {
        private val TAG = MovieDetailsFragment::class.java.simpleName
        fun newInstance(movieId: String) = MovieDetailsFragmentV2().apply {
            arguments = Bundle().apply {
                putString(MOVIE_ID, movieId)
            }
        }

        private const val MOVIE_ID = "movie_id"
    }
}