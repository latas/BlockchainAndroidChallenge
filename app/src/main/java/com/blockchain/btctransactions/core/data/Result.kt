package com.blockchain.btctransactions.core.data

sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    val isLoading: Boolean
        get() = when (this) {
            is Loading -> true
            else -> false
        }
}
