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
fun SwipeRefreshLayout.isRefreshing(refreshStopped: Unit?) {
    refreshStopped?.let {
        this.isRefreshing = false
    }
}

@BindingAdapter("refresh")
fun SwipeRefreshLayoutWithStateListener.setRefresh(refreshing: Boolean) {

}

@BindingAdapter("refreshAttrChanged")
fun SwipeRefreshLayoutWithStateListener.setOnRefreshStateChanged(bindingListener: InverseBindingListener) {
    refreshStateListener = object : RefreshStateListener {
        override fun onRefreshStateChanged(refreshing: Boolean) {
            bindingListener.onChange()
        }
    }
}

@InverseBindingAdapter(attribute = "refresh")
fun isRefreshing(swipeRefreshLayout: SwipeRefreshLayoutWithStateListener): Boolean =
    swipeRefreshLayout.isRefreshing
