package com.blockchain.btctransactions.core.data

sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
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
}
