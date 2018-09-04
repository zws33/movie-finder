package me.zwsmith.moviefinder.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.zwsmith.moviefinder.R
import me.zwsmith.moviefinder.presentation.movieResults.MovieResultsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
                .beginTransaction()
                .add(R.id.root, MovieResultsFragment.newInstance())
                .commit()
    }
}