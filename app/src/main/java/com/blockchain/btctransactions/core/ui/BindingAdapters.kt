package com.blockchain.btctransactions.core.ui

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@BindingAdapter("visibility")
fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("stopRefresh")
fun SwipeRefreshLayout.isRefreshing(refreshStopped: Unit?) {
    refreshStopped?.let {
        this.isRefreshing = false
    }
}