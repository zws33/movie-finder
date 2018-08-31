package me.zwsmith.moviefinder.presentation.common

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log


abstract class EndlessRecyclerOnScrollListener : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy > 0) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = recyclerView.adapter.itemCount
            val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()

            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                Log.v("...", "Last Item Wow !")
                requestData()
            }
        }
    }

    abstract fun requestData()
}