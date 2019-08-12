package me.zwsmith.moviefinder.presentation.common

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.zwsmith.moviefinder.presentation.extensions.inflateView

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseViewHolder<out T>>() {

    private var data: List<T> = emptyList()

    abstract fun getViewType(viewState: T): Int
    abstract fun getViewHolder(viewType: Int, view: View): BaseViewHolder<out T>

    fun updateItems(newData: List<T>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<out T> {
        return getViewHolder(viewType, parent.context.inflateView(viewType, parent))
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return getViewType(data[position])
    }

    override fun onBindViewHolder(holder: BaseViewHolder<out T>, position: Int) {
        @Suppress("UNCHECKED_CAST")
        (holder as BaseViewHolder<T>).bind(data[position])
    }

}

abstract class BaseViewHolder<T>(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(viewState: T)
}
