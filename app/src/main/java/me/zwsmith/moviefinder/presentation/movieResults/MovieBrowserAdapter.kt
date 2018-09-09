package me.zwsmith.moviefinder.presentation.movieResults


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.movie_results_item.view.*
import me.zwsmith.moviefinder.R
import me.zwsmith.moviefinder.presentation.extensions.inflate

class MovieBrowserAdapter(
        private val viewStateStream: Observable<List<MovieItemViewState>>,
        private val itemOnClick: (String) -> Unit
) : RecyclerView.Adapter<MovieBrowserAdapter.MovieHolder>() {
    private val viewState: MutableList<MovieItemViewState> = mutableListOf()

    init {
        viewStateStream
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    viewState.addAll(it)
                    notifyDataSetChanged()
                }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = parent.inflate(R.layout.movie_results_item)
        return MovieHolder(view, itemOnClick)
    }

    override fun getItemCount(): Int = viewState.size

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.bindViewState(viewState[position])
    }

    class MovieHolder(
            private val view: View,
            private val onClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private var id: String? = null

        init {
            view.setOnClickListener { _ ->
                id?.let { onClick(it) }
            }
        }

        fun bindViewState(viewState: MovieItemViewState) {
            id = viewState.id

            Picasso.get()
                    .load(viewState.imageUrl)
                    .into(view.poster_icon)

            view.title_tv.text = viewState.title
            view.popularity_tv.text = viewState.popularity
        }
    }
}