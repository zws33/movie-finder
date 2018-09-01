package me.zwsmith.moviefinder.presentation.movieResults


import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_results_item.view.*
import me.zwsmith.moviefinder.R
import me.zwsmith.moviefinder.presentation.extensions.inflate

class MovieResultsAdapter(
        private val movieResultsViewStates: ArrayList<MovieResultsItemViewState>
) : RecyclerView.Adapter<MovieResultsAdapter.MovieHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = parent.inflate(R.layout.movie_results_item)
        return MovieHolder(view)
    }

    override fun getItemCount(): Int = movieResultsViewStates.size

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.bindViewState(movieResultsViewStates[position])
    }

    class MovieHolder(
            private var view: View
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private var id: String? = null

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Toast.makeText(view.context, "Movie ID: $id", Toast.LENGTH_SHORT).show()
        }

        fun bindViewState(viewState: MovieResultsItemViewState) {
            Picasso.get()
                    .load(viewState.imageUrl)
                    .into(view.poster_icon)

            view.title_tv.text = viewState.title
            view.genres_tv.text = viewState.genres
        }
    }
}