package com.blockchain.btctransactions.core.data

interface ResourceFacade {
    fun getString(resource: Int): String
    fun getString(resource: Int, vararg strings: String): String
}