package me.zwsmith.moviefinder.presentation.extensions


import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun TextView.setTextOrHide(text: String?) {
    this.setTextOrHide(this, text)
}

fun TextView.setTextOrHide(view: View, text: String?) {
    if (text.isNullOrBlank())
        view.visibility = View.GONE
    else {
        this.text = text
        view.visibility = View.VISIBLE
    }
}