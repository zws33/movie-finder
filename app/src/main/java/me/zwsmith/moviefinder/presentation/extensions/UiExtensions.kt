package me.zwsmith.moviefinder.presentation.extensions


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import me.zwsmith.moviefinder.core.dependencyInjection.MoveFinderApplication
import me.zwsmith.moviefinder.core.dependencyInjection.dagger.ViewModelFactory

fun Fragment.getInjector() = (this.activity!!.application as MoveFinderApplication).injector

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

inline fun <reified T : ViewModel> Fragment.getViewModel(viewModelFactory: ViewModelFactory): T {
    return ViewModelProviders.of(this, viewModelFactory)[T::class.java]
}