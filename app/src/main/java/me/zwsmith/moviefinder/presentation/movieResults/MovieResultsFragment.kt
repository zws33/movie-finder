package me.zwsmith.moviefinder.presentation.movieResults

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_movie_results.view.*
import me.zwsmith.moviefinder.R
import me.zwsmith.moviefinder.presentation.extensions.isVisible

class MovieResultsFragment : Fragment() {

    private val movieListViewState = arrayListOf<MovieResultsItemViewState>()
    private val movieListAdapter = MovieResultsAdapter(movieListViewState)

    private lateinit var viewModel: MovieResultsViewModel
    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MovieResultsViewModel::class.java)
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
            adapter = movieListAdapter
        }
    }


    override fun onStart() {
        super.onStart()
        val movieListViewStateStream = viewModel.getMovieListViewStateStream()
        compositeDisposable.add(
                movieListViewStateStream
                        .doOnNext {
                            Log.d(TAG, it.toString())
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                                onNext = { view?.update(it) },
                                onError = { Log.e(TAG, "Error message: ${it.message}", it) }
                        )
        )

        viewModel.getPopularMovies()

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun View.update(viewState: MovieResultsViewState) {
        progress.isVisible = viewState.isLoadingVisible
        error_group.isVisible = viewState.isErrorVisible
        movie_results_rv.isVisible = viewState.movieResults != null
        viewState.movieResults?.let {
            movieListViewState.addAll(ArrayList(it))
            movieListAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        private val TAG = MovieResultsFragment::class.java.simpleName

        fun newInstance(): Fragment {
            return MovieResultsFragment()
        }
    }
}