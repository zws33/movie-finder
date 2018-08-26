package me.zwsmith.moviefinder.presentation.movieResults


import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_list_item.view.*
import me.zwsmith.moviefinder.R
import me.zwsmith.moviefinder.presentation.extensions.inflate

/**
 * Created by RBI Engineers on 8/25/18.
 */
class MovieListAdapter(
        private val movieListViewStates: ArrayList<MovieListItemViewState>
) : RecyclerView.Adapter<MovieListAdapter.MovieHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = parent.inflate(R.layout.movie_list_item)
        return MovieHolder(view)
    }

    override fun getItemCount(): Int = movieListViewStates.size

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.bindViewState(movieListViewStates[position])
    }


    class MovieHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var view: View = v
        private var viewState: MovieListItemViewState? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Log.d("RecyclerView", viewState?.title)
        }

        fun bindViewState(viewState: MovieListItemViewState) {
            this.viewState = viewState
            Picasso.get().load(viewState.imageUrl).into(view.poster_icon)
            view.title_tv.text = viewState.title
            view.genres_tv.text = viewState.genres
        }

    }
}


data class MovieListItemViewState(
        val title: String,
        val genres: String,
        val imageUrl: String
)