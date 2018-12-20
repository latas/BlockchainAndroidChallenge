package com.blockchain.btctransactions.core.ui

import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
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

@BindingAdapter("background")
fun View.background(background: Int) {
    if (background == 0)
        return
    this.background = ContextCompat.getDrawable(context, background)
}
