package me.zwsmith.moviefinder.presentation.extensions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso

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

fun Context.inflateView(layout: Int, parent: ViewGroup): View =
    LayoutInflater.from(this).inflate(layout, parent, false)

fun TextInputEditText.onSubmit(func: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            func()
        }
        true
    }
}

fun <T : Fragment> T.withArgs(block: Bundle.() -> Unit): T {
    return this.apply {
        arguments = Bundle().apply { block() }
    }
}

fun <T> Fragment.observe(liveData: LiveData<T>, observer: (T) -> Unit) {
    liveData.observe(this, Observer<T>(observer))
}

/**
 * Extension function to wrap usage of picasso for image loading.
 */
fun ImageView.loadImage(imageUrl: String) {
    Picasso.get()
        .load(imageUrl)
        .into(this)
}



