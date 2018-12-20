package com.blockchain.btctransactions.core.utils

import com.blockchain.btctransactions.core.data.Result
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import java.util.concurrent.TimeUnit


fun <T : Any> Single<T>.toResult(): Observable<Result<T>> =
    this.toObservable().map<Result<T>> {
        Result.Success(it)
    }.onErrorReturn {
        Result.Error(it)
    }

fun <T : Any> Observable<Result<T>>.withLoading(): Observable<Result<T>> =
    this.zipWith(Observable.timer(1, TimeUnit.SECONDS)) { res, _ -> res }.startWith(Result.Loading)