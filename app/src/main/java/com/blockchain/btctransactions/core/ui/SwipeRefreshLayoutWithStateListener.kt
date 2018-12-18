package com.blockchain.btctransactions.core.ui

import android.content.Context
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class SwipeRefreshLayoutWithStateListener @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {

    var refreshStateListener: RefreshStateListener? = null

    override fun setRefreshing(refreshing: Boolean) {
        super.setRefreshing(refreshing)
        refreshStateListener?.onRefreshStateChanged(refreshing)
    }
}

interface RefreshStateListener {
    fun onRefreshStateChanged(refreshing: Boolean)
}