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
            this.postValue(lastA to lastB)
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
        singleLiveData.postValue(it)
    }
    return singleLiveData
}

fun <A, B> LiveData<A>.withLatestFrom(b: LiveData<B>): LiveData<Pair<A?, B?>> {
    return MediatorLiveData<Pair<A?, B?>>().apply {

        var lastA: A? = null
        var lastB: B? = null

        fun update() {
            this.postValue(lastA to lastB)
        }

        addSource(this@withLatestFrom) {
            lastA = it
            update()
        }
        addSource(b) {
            lastB = it
        }
    }
}

inline fun <T> LiveData<T>.filter(crossinline predicate: (T?) -> Boolean): LiveData<T> {
    val mutableLiveData: MediatorLiveData<T> = MediatorLiveData()
    mutableLiveData.addSource(this) {
        if (predicate(it))
            mutableLiveData.value = it
    }
    return mutableLiveData
}