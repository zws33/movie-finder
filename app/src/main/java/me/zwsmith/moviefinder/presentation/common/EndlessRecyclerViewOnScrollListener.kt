package me.zwsmith.moviefinder.presentation.common

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView


abstract class EndlessRecyclerOnScrollListener : RecyclerView.OnScrollListener() {
    private var previousTotal = 0
    private var loading = true
    private val visibleThreshold = 10
    private var lastVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView.childCount
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        totalItemCount = layoutManager.itemCount
        lastVisibleItem = layoutManager.findLastVisibleItemPosition()

        if (dy > 0) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false
                    previousTotal = totalItemCount
                }
            }

            if (!loading && (totalItemCount - visibleItemCount) <= (lastVisibleItem + visibleThreshold)) {
                requestData()
                loading = true
            }
        }
    }

    abstract fun requestData()

    companion object {
        private val TAG = EndlessRecyclerOnScrollListener::class.java.simpleName
    }
}