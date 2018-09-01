package me.zwsmith.moviefinder.presentation.movieResults

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
import me.zwsmith.moviefinder.core.dependencyInjection.ViewModelFactory
import me.zwsmith.moviefinder.presentation.common.EndlessRecyclerOnScrollListener
import me.zwsmith.moviefinder.presentation.extensions.getInjector
import me.zwsmith.moviefinder.presentation.extensions.getViewModel
import me.zwsmith.moviefinder.presentation.extensions.isVisible
import me.zwsmith.moviefinder.presentation.movieDetails.MovieDetailsFragment
import javax.inject.Inject

class MovieResultsFragment : Fragment() {

    private val movieListViewState = mutableListOf<MovieItemViewState>()
    private val movieListAdapter = MovieResultsAdapter(movieListViewState, ::navigateToDetails)

    private lateinit var viewModel: MovieResultsViewModel
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
        return inflater.inflate(R.layout.fragment_movie_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view.movie_results_rv) {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            adapter = movieListAdapter
            addOnScrollListener(onScrollListener)
        }
    }

    private fun navigateToDetails(movieId: String) {
        fragmentManager
                ?.beginTransaction()
                ?.replace(R.id.root, MovieDetailsFragment.newInstance(movieId))
                ?.addToBackStack(null)
                ?.commit()
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable.add(
                viewModel.movieListViewStateStream
                        .doOnNext { Log.d(TAG, it.toString()) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                                onNext = { view?.update(it) },
                                onError = { Log.e(TAG, "Error message: ${it.message}", it) }
                        )
        )
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
        view?.movie_results_rv?.invalidate()
    }

    private fun View.update(viewState: MovieResultsViewState) {
        progress.isVisible = viewState.isLoadingVisible
        error_group.isVisible = viewState.isErrorVisible
        movie_results_rv.isVisible = viewState.movieResults != null
        viewState.movieResults?.let {
            movieListViewState.addAll(it)
            movieListAdapter.notifyDataSetChanged()
        }
    }

    private val onScrollListener = object : EndlessRecyclerOnScrollListener() {
        override fun requestData() {
            viewModel.loadNextPage()
        }
    }

    companion object {
        private val TAG = MovieResultsFragment::class.java.simpleName

        fun newInstance(): Fragment {
            return MovieResultsFragment()
        }
    }
}