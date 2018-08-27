package me.zwsmith.moviefinder.core.common

import io.reactivex.Observable
import io.reactivex.Single

sealed class Result<out T> {
    object Pending : Result<Nothing>() {
        override fun toString(): String = "Result.Pending"
    }

    sealed class Complete<out T> : Result<T>() {
        data class Success<out T>(val value: T) : Complete<T>()
        data class Error<out T>(val error: Throwable) : Complete<T>()
    }
}

fun <T> T.toSuccess(): Result<T> = Result.Complete.Success(this)

fun <T> Throwable.toError(): Result<T> = Result.Complete.Error(this)

fun <T> T.toSuccessComplete(): Result.Complete<T> = Result.Complete.Success(this)

fun <T> Throwable.toErrorComplete(): Result.Complete<T> = Result.Complete.Error(this)

fun <T, U> Result<T>.resultMap(mapFunc: (T) -> U): Result<U> =
        when (this) {
            is Result.Complete.Success -> mapFunc(this.value).toSuccess()
            is Result.Complete.Error -> this.error.toError()
            is Result.Pending -> Result.Pending
        }

fun <T, U> Result.Complete<T>.resultMapComplete(mapFunc: (T) -> U): Result.Complete<U> =
        when (this) {
            is Result.Complete.Success -> mapFunc(this.value).toSuccessComplete()
            is Result.Complete.Error -> this.error.toErrorComplete()
        }

fun <T, U> Result<T>.flatMapResult(mapFunc: (T) -> Result<U>): Result<U> = when (this) {
    is Result.Complete.Success -> mapFunc(this.value)
    is Result.Complete.Error -> this.error.toError()
    Result.Pending -> Result.Pending
}

fun <T> Observable<T>.wrapInResult(): Observable<Result<T>> =
        this.map { it.toSuccess() }.onErrorReturn { it.toError() }

fun <T> Single<T>.wrapInResult(): Single<Result<T>> =
        this.map { it.toSuccess() }.onErrorReturn { it.toError() }