package me.zwsmith.moviefinder.presentation.movieDetails

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import me.zwsmith.moviefinder.R
import me.zwsmith.moviefinder.core.dependencyInjection.ViewModelFactory
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
                    viewModel.getMovieDetailsSingle(it)
                            .subscribeBy(
                                    onSuccess = { movieDetailsResponse ->
                                        Log.d(TAG, movieDetailsResponse.title)
                                    },
                                    onError = { e -> Log.e(TAG, e.message, e) }
                            )
            )
        }
    }

    companion object {
        private val TAG = MovieDetailsFragment::class.java.simpleName
        fun newInstance(movieId: String) = MovieDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(MOVIE_ID, movieId)
            }
        }

        private const val MOVIE_ID = "MOVIE_ID"
    }
}