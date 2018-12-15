package com.blockchain.btctransactions.core.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.blockchain.btctransactions.core.data.Result

fun <T, U> LiveData<Result<T>>.mapSuccess(func: (T) -> U): LiveData<U> {
    val mutableLiveData: MediatorLiveData<U> = MediatorLiveData()
    mutableLiveData.addSource(this) {
        when (it) {
            is Result.Success -> {
                mutableLiveData.postValue(func(it.data))
            }
        }
    }
    return mutableLiveData
}
