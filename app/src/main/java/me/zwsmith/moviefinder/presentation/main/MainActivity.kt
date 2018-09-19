package me.zwsmith.moviefinder.presentation.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import me.zwsmith.moviefinder.R
import me.zwsmith.moviefinder.presentation.movieBrowser.MovieBrowserFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
                .beginTransaction()
                .add(R.id.root, MovieBrowserFragment.newInstance())
                .commit()
    }
}