package me.zwsmith.moviefinder.presentation.movieBrowser

import android.view.View
import com.squareup.picasso.Picasso
import me.zwsmith.moviefinder.R
import me.zwsmith.moviefinder.presentation.common.BaseAdapter

class MovieBrowserAdapter(private val picasso: Picasso) : BaseAdapter<MovieBrowserViewState.RowViewState>() {
    override fun getViewType(viewState: MovieBrowserViewState.RowViewState): Int {
        return R.layout.item_movie_list_row
    }

    override fun getViewHolder(viewType: Int, view: View): MovieBrowserRow {
        return MovieBrowserRow(view, picasso)
    }
}