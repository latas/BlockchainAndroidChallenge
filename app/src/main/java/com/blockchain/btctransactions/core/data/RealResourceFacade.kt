package com.blockchain.btctransactions.core.data

import android.content.Context

class RealResourceFacade(val context: Context) : ResourceFacade {
    override fun getString(resource: Int): String =
        context.resources.getString(resource)


    override fun getString(resource: Int, vararg strings: String): String =
        context.resources.getString(resource, strings)
}