package me.zwsmith.moviefinder.core.common

import android.util.Log
import me.zwsmith.moviefinder.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ApiKeyInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalUrl = request.url()

        val newUrl = originalUrl
                .newBuilder()
                .addQueryParameter("api_key", BuildConfig.ApiKey)
                .build()
                .also { Log.d(TAG, "New url: $it") }

        val newRequest = request
                .newBuilder()
                .url(newUrl)
                .build()

        return chain.proceed(newRequest)
    }

    companion object {
        private val TAG = ApiKeyInterceptor::class.java.simpleName
    }
}