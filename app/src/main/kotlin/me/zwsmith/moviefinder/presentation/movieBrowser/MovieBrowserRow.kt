package me.zwsmith.moviefinder.presentation.movieBrowser

import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie_list_row.view.*
import me.zwsmith.moviefinder.R
import me.zwsmith.moviefinder.presentation.common.BaseViewHolder

class MovieBrowserRow(view: View, private val picasso: Picasso) : BaseViewHolder<MovieBrowserViewState.RowViewState>(view) {
    private lateinit var id: String

    override fun bind(viewState: MovieBrowserViewState.RowViewState) {
        id = viewState.id

        picasso
            .load(viewState.imageUrl)
            .placeholder(R.drawable.ic_action_movie)
            .fit()
            .into(itemView.poster_icon)

        itemView.title_tv.text = viewState.title
        itemView.rating.text = viewState.rating
        itemView.genres.text = viewState.genres.joinToString(", ")
        itemView.setOnClickListener { viewState.onClick() }
    }
}