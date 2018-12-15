package com.blockchain.btctransactions.core.ui

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibility")
fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}