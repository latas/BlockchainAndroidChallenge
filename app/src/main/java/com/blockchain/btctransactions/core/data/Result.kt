package com.blockchain.btctransactions.core.data

sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val throwable: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()

    val isLoading: Boolean
        get() = when (this) {
            is Loading -> true
            else -> false
        }

    val isSuccess: Boolean
        get() = when (this) {
            is Success -> true
            else -> false
        }

    val isError: Boolean
        get() = when (this) {
            is Error -> true
            else -> false
        }
}
