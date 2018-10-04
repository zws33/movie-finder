package me.zwsmith.moviefinder.core.common

import android.util.Log
import javax.inject.Inject

class Logger @Inject constructor() {
    fun v(tag: String, msg: String) = Log.v(tag, msg)
    fun d(tag: String, msg: String) = Log.d(tag, msg)
    fun i(tag: String, msg: String) = Log.i(tag, msg)
    fun w(tag: String, msg: String) = Log.w(tag, msg)
    fun w(tag: String, msg: String, throwable: Throwable) = Log.w(tag, msg, throwable)
    fun e(tag: String, msg: String) = Log.e(tag, msg)
    fun e(tag: String, msg: String, throwable: Throwable) = Log.e(tag, msg, throwable)
}