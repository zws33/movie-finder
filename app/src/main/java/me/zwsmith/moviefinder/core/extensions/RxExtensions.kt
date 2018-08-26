package me.zwsmith.moviefinder.core.extensions

import io.reactivex.Observable
import io.reactivex.rxkotlin.zipWith
import java.util.concurrent.TimeUnit

/**
 * Created by RBI Engineers on 8/26/18.
 */
fun <T> T.just() = Observable.just(this)!!

fun <T> Iterable<T>.from() = Observable.fromIterable(this)!!

fun <T> emitAtInterval(events: List<T>,
                       intervalSeconds: Long = 1,
                       initialEvent: T? = null
): Observable<T> {
    val interval = Observable.interval(intervalSeconds, TimeUnit.SECONDS)
    val eventStream = events
            .from()
            .zipWith(interval) { event, _ -> event }

    return initialEvent?.let { eventStream.startWith(it) } ?: eventStream
}