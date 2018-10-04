package me.zwsmith.moviefinder.core.services

import com.nhaarman.mockitokotlin2.mock
import okhttp3.OkHttpClient
import org.junit.Assert
import org.junit.Test

class MovieServiceImplTest {

    @Test
    fun getPopularMovies_assertUrlCorrect() {
        //Given a MovieServiceImpl
        val okHttpClient = mock<OkHttpClient>()
        val movieServiceImpl = MovieServiceImpl(
                okHttpClient = okHttpClient,
                gson = mock(),
                logger = mock()
        )

        //When get getPopularMovies is called
        val pageNumber = 1
        val testUrl = movieServiceImpl.buildPopularMoviesUrl(pageNumber)


        //Then url should match expected url
        val expectedUrl =
                "https://api.themoviedb.org/3/discover/movie?language=en-US&sort_by=popularity.desc&page=1"
        Assert.assertEquals(testUrl.toString(), expectedUrl)
    }
}