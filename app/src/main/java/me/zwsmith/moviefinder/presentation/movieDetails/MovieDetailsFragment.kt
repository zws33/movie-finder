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
import me.zwsmith.moviefinder.core.dependencyInjection.dagger.ViewModelFactory
import me.zwsmith.moviefinder.presentation.extensions.getInjector
import me.zwsmith.moviefinder.presentation.extensions.getViewModel
import javax.inject.Inject

class MovieDetailsFragment : Fragment() {

    private lateinit var viewModel: MovieDetailsViewModel
    private var compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getInjector().inject(this)
        viewModel = getViewModel(viewModelFactory)
    }

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
                    viewModel.getMovieDetailsViewState(it)
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
        fun newInstance(movieId: String) = MovieDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(MOVIE_ID, movieId)
            }
        }

        private const val MOVIE_ID = "movie_id"
    }
}