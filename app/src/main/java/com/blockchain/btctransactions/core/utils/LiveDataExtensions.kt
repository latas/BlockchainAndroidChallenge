package com.blockchain.btctransactions.core.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.blockchain.btctransactions.core.data.Result

fun <T : Any, U> LiveData<Result<T>>.mapSuccess(func: (T) -> U): LiveData<U> {
    val mediatorLiveData: MediatorLiveData<U> = MediatorLiveData()
    mediatorLiveData.addSource(this) {
        when (it) {
            is Result.Success -> {
                mediatorLiveData.postValue(func(it.data))
            }
        }
    }
    return mediatorLiveData
}

fun <A, B> LiveData<A>.merge(b: LiveData<B>): LiveData<Pair<A?, B?>> = mergeLiveData(this, b)

fun <A, B> mergeLiveData(a: LiveData<A>, b: LiveData<B>): LiveData<Pair<A?, B?>> {
    return MediatorLiveData<Pair<A?, B?>>().apply {

        var lastA: A? = null
        var lastB: B? = null

        fun update() {
            this.value = lastA to lastB
        }

        addSource(a) {
            lastA = it
            update()
        }
        addSource(b) {
            lastB = it
            update()
        }
    }
}

fun <T> LiveData<T>.single(): LiveData<T> {
    val singleLiveData = SingleLiveEvent<T>()
    singleLiveData.addSource(this) {
        singleLiveData.value = it
    }
    return singleLiveData
}

fun <T : Any> LiveData<T?>.filterNulls(): LiveData<T> {
    val mediatorLiveData: MediatorLiveData<T> = MediatorLiveData()
    mediatorLiveData.addSource(this) {
        if (it != null) {
            mediatorLiveData.postValue(value)
        }
    }
    return mediatorLiveData
}
