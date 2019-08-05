package me.zwsmith.moviefinder.core.common

import io.reactivex.Observable
import io.reactivex.Single

sealed class ResponseStatus<out T> {
    object Pending : ResponseStatus<Nothing>() {
        override fun toString(): String = "ResponseStatus.Pending"
    }

    sealed class Complete<out T> : ResponseStatus<T>() {
        data class Success<out T>(val value: T) : Complete<T>()
        data class Error<out T>(val error: Throwable) : Complete<T>()
    }
}

fun <T> T.toSuccess(): ResponseStatus<T> = ResponseStatus.Complete.Success(this)

fun <T> Throwable.toError(): ResponseStatus<T> = ResponseStatus.Complete.Error(this)

fun <T> T.toSuccessComplete(): ResponseStatus.Complete<T> = ResponseStatus.Complete.Success(this)

fun <T> Throwable.toErrorComplete(): ResponseStatus.Complete<T> = ResponseStatus.Complete.Error(this)

fun <T, U> ResponseStatus<T>.mapResponse(mapFunc: (T) -> U): ResponseStatus<U> =
        when (this) {
            is ResponseStatus.Complete.Success -> mapFunc(this.value).toSuccess()
            is ResponseStatus.Complete.Error -> this.error.toError()
            is ResponseStatus.Pending -> ResponseStatus.Pending
        }

fun <T, U> ResponseStatus.Complete<T>.mapResponseComplete(mapFunc: (T) -> U): ResponseStatus.Complete<U> =
        when (this) {
            is ResponseStatus.Complete.Success -> mapFunc(this.value).toSuccessComplete()
            is ResponseStatus.Complete.Error -> this.error.toErrorComplete()
        }

fun <T, U> ResponseStatus<T>.flatMapResponse(mapFunc: (T) -> ResponseStatus<U>): ResponseStatus<U> = when (this) {
    is ResponseStatus.Complete.Success -> mapFunc(this.value)
    is ResponseStatus.Complete.Error -> this.error.toError()
    ResponseStatus.Pending -> ResponseStatus.Pending
}

fun <T> Observable<T>.wrapResponse(): Observable<ResponseStatus<T>> =
        this.map { it.toSuccess() }.onErrorReturn { it.toError() }

fun <T> Single<T>.wrapResponse(): Single<ResponseStatus<T>> =
        this.map { it.toSuccess() }.onErrorReturn { it.toError() }