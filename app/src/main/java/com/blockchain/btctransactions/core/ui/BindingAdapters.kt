package com.blockchain.btctransactions.core.ui

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@BindingAdapter("visibility")
fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("stopRefresh")
fun SwipeRefreshLayout.isRefreshing(refreshStopped: Boolean) {
    if (refreshStopped) {
        isRefreshing = false
    }
}