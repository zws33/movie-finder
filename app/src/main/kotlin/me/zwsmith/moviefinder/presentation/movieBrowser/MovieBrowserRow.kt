package me.zwsmith.moviefinder.presentation.movieBrowser

import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie_list_row.view.*
import me.zwsmith.moviefinder.presentation.common.BaseViewHolder

class MovieBrowserRow(view: View) : BaseViewHolder<MovieBrowserViewState.RowViewState>(view) {
    private lateinit var id: String

    override fun bind(viewState: MovieBrowserViewState.RowViewState) {
        id = viewState.id

        Picasso.get()
            .load(viewState.imageUrl)
            .into(itemView.poster_icon)

        itemView.title_tv.text = viewState.title
        itemView.popularity_tv.text = viewState.popularity
        itemView.setOnClickListener { viewState.onClick() }
    }
}